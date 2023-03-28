package markdown.processor.element

import external.TexRender
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.type.ObsidianTokenTypes
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.ast.impl.ListCompositeNode
import org.intellij.markdown.ast.impl.ListItemCompositeNode
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.LiHTMLAttributes
import react.dom.html.ReactHTML.li
import web.html.HTMLElement
import web.html.HTMLLIElement

class ListItemElementProcessor<Parent>(texRender: TexRender?) :
    SimpleElementNodeProcessor<
        Parent,
        HTMLLIElement,
        LiHTMLAttributes<HTMLLIElement>,
        IntrinsicType<LiHTMLAttributes<HTMLLIElement>>,
        >(li) where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    private val texProcessor = TexElementProcessor<Parent>(texRender)
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        if (node !is ListItemCompositeNode) error("Incorrect node type Expected: ListItemCompositeNode but: $node")

        openTag(visitor, markdownText, node)

        val listNode = node.parent
        if (listNode !is ListCompositeNode) error("Incorrect parent node type Expected: ListCompositeNode but: $listNode")
        val isLoose = listNode.loose
        node.children.forEach { child ->
            // Since it is impossible for TexParser to detect formulas in the list, it searches for them here.
            if (child.type == MarkdownElementTypes.PARAGRAPH && !isLoose) {
                var isInMathText = false
                var startPos = 0
                child.children.forEachIndexed { index, it ->
                    if (it.type == ObsidianTokenTypes.DOLLAR) {
                        if (!isInMathText) {
                            startPos = index
                            isInMathText = true
                        } else {
                            isInMathText = false
                            val texNode = object : ASTNode by child {
                                override val children: List<ASTNode> = child.children.subList(startPos, index)
                            }
                            texProcessor.processNode(visitor, markdownText, texNode)
                        }
                    } else if (!isInMathText) {
                        // This process is the same as InlineHolderNodeProcessor
                        if (it is LeafASTNode) {
                            visitor.visitLeaf(it)
                        } else {
                            it.accept(visitor)
                        }
                    }
                }
            } else {
                child.accept(visitor)
            }
        }

        closeTag(visitor, markdownText, node)
    }
}
