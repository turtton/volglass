package markdown.processor.element

import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.SilentParagraphNodeProcessor
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.ast.impl.ListCompositeNode
import org.intellij.markdown.ast.impl.ListItemCompositeNode
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLIElement
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.LiHTMLAttributes
import react.dom.html.ReactHTML.li

class ListItemElementProcessor<Parent : HTMLAttributes<HTMLElement>> :
    SimpleElementNodeProcessor<
        Parent,
        HTMLLIElement,
        LiHTMLAttributes<HTMLLIElement>,
        IntrinsicType<LiHTMLAttributes<HTMLLIElement>>,
        >(li) {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        if (node !is ListItemCompositeNode) error("Incorrect node type Expected: ListItemCompositeNode but: $node")

        openTag(visitor, markdownText, node)

        val listNode = node.parent
        if (listNode !is ListCompositeNode) error("Incorrect parent node type Expected: ListCompositeNode but: $listNode")
        val isLoose = listNode.loose
        node.children.forEach { child ->
            if (child.type == MarkdownElementTypes.PARAGRAPH && !isLoose) {
                SilentParagraphNodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>()
                    .processNode(visitor, markdownText, child)
            } else {
                child.accept(visitor)
            }
        }

        closeTag(visitor, markdownText, node)
    }
}
