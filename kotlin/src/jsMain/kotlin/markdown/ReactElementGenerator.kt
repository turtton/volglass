package markdown

import markdown.processor.NodeProcessor
import org.intellij.markdown.IElementType
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.visitors.Visitor
import org.intellij.markdown.html.HtmlGenerator
import react.ChildrenBuilder
import react.ElementType
import react.FC
import react.Props
import react.dom.html.HTMLAttributes
import web.html.HTMLElement

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
        private val elementLists = mutableListOf<MutableList<ChildrenBuilder.() -> Unit>>()
        private val tagStacker = mutableListOf<ElementType<HTMLAttributes<HTMLElement>>>()
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
            println("Type: ${node.type}")
            providers[node.type]?.processNode(this, markdownText, node)
                ?: node.acceptChildren(this)
        }

        override fun visitLeaf(node: ASTNode) {
            println("lType: ${node.type}")
            providers[node.type]?.processNode(this, markdownText, node)
                ?: run {
                    val leafText = HtmlGenerator.leafText(markdownText, node)
                    consume {
                        +leafText.toString()
                    }
                }
        }

        override fun consumeTagOpen(node: ASTNode, tag: ElementType<HTMLAttributes<HTMLElement>>, autoClose: Boolean) {
            tagStacker += tag
            elementLists.add(mutableListOf())
        }

        override fun consumeTagClose(tag: ElementType<HTMLAttributes<HTMLElement>>) {
            val target = tagStacker.removeLast()
            if (target != tag) error("Trying to close $tag but actual $target")
            val child = elementLists.removeLast()
            val element: ChildrenBuilder.() -> Unit = {
                target {
                    child.forEach {
                        it()
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
            elementLists.last() += {
                @Suppress("UNCHECKED_CAST")
                (this as Parent).invoker()
            }
        }
    }
}
