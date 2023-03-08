package markdown.processor

import markdown.TagConsumer
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes

/**
 * [CommonMarkFlavourDescriptor]:L54(MarkdownElementTypes.HTML_BLOCK)
 */
class HtmlBlockElementProcessor<Parent> :
    NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor {
        visitor.consume {
            node.children.forEach {
                if (it.type in listOf(MarkdownTokenTypes.EOL, MarkdownTokenTypes.HTML_BLOCK_CONTENT)) {
                    +it.getTextInNode(markdownText).toString()
                }
            }
        }
    }
}
