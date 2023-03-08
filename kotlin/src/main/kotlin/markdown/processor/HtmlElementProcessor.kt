package markdown.processor

import markdown.TagConsumer
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes

/**
 * Related [CommonMarkFlavourDescriptor]:L55-59(MarkdownTokenTypes.HTML_TAG)
 */
class HtmlElementProcessor<Parent> : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor {
        visitor.consume {
            +node.getTextInNode(markdownText).toString()
        }
    }
}
