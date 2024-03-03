package markdown.processor.element

import js.objects.jso
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import web.html.HTMLElement

/**
 * Related [org.intellij.markdown.html.HtmlBlockGeneratingProvider]
 */
@Suppress("KDocUnresolvedReference")
class HtmlBlockElementProcessor<Parent> :
    NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val html = node.children.joinToString(separator = "") {
            if (it.type in listOf(MarkdownTokenTypes.EOL, MarkdownTokenTypes.HTML_BLOCK_CONTENT)) {
                it.getTextInNode(markdownText).toString()
            } else {
                ""
            }
        }
        visitor.consume {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = html
                }
            }
        }
    }
}
