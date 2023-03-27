package markdown.processor.element

import js.core.jso
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import web.html.HTMLElement

/**
 * Related [CommonMarkFlavourDescriptor]:L55-59(MarkdownTokenTypes.HTML_TAG)
 */
class HtmlElementProcessor<Parent> : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val html = node.getTextInNode(markdownText)
        visitor.consume {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = html.toString()
                }
            }
        }
    }
}
