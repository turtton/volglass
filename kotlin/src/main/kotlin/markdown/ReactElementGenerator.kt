package markdown

import markdown.processor.NodeProcessor
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.visitors.Visitor
import org.intellij.markdown.html.HtmlGenerator
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.ElementType
import react.FC
import react.Props
import react.dom.html.HTMLAttributes

class ReactElementGenerator<Parent>(
    private val markdownText: String,
    private val rootNode: ASTNode,
    private val providers: Map<IElementType, NodeProcessor<ElementType<HTMLAttributes<HTMLElement>>, Parent>>,
) where Parent : ChildrenBuilder, Parent : HTMLAttributes<HTMLElement> {
    fun generateElement(): FC<Props> {
        return ReactElementGeneratingVisitor(providers, markdownText).let {
            it.visitNode(rootNode)
            it.result
        }
    }

    class ReactElementGeneratingVisitor<Parent>(
        private val providers: Map<IElementType, NodeProcessor<ElementType<HTMLAttributes<HTMLElement>>, Parent>>,
        private val markdownText: String,
    ) : TagConsumer<ElementType<HTMLAttributes<HTMLElement>>, Parent>, Visitor, LeafVisitor where Parent : ChildrenBuilder, Parent : HTMLAttributes<HTMLElement> {
        private val elementLists = mutableListOf<MutableList<FC<Props>>>()
        private val tagStacker = mutableListOf<ElementType<HTMLAttributes<HTMLElement>>>()
        private val elementProcessor = mutableListOf<Parent.() -> Unit>()
        val result: FC<Props> get() = elementLists.toList().let { results ->
            FC {
                results.forEach { elements ->
                    elements.forEach {
                        it()
                    }
                }
            }
        }
        override fun visitNode(node: ASTNode) {
            providers[node.type]?.processNode(this, markdownText, node)
                ?: node.acceptChildren(this)
        }

        override fun visitLeaf(node: ASTNode) {
            providers[node.type]?.processNode(this, markdownText, node)
                ?: consume {
                    +HtmlGenerator.leafText(markdownText, node).toString()
                }
        }

        override fun consumeTagOpen(node: ASTNode, tag: ElementType<HTMLAttributes<HTMLElement>>, autoClose: Boolean) {
            tagStacker += tag
            elementProcessor += {}
            elementLists.add(mutableListOf())
        }

        override fun consumeTagClose(tag: ElementType<HTMLAttributes<HTMLElement>>) {
            val target = tagStacker.removeLast()
            if (target != tag) error("Trying to close $tag but actual $target")
            val child = elementLists.removeLast()
            val processor = elementProcessor.removeLast()
            val element = FC<Props> {
                target {
                    @Suppress("UNCHECKED_CAST")
                    val parent = this as Parent
                    parent.processor()
                    child.forEach {
                        it {}
                    }
                }
            }
            if (elementLists.isEmpty()) {
                elementLists.add(mutableListOf(element))
            } else {
                elementLists.last() += element
            }
        }

        override fun consume(invoker: Parent.() -> Unit) {
            val lastProcess = elementProcessor.removeLast()
            elementProcessor += {
                lastProcess()
                invoker()
            }
        }
    }
}
