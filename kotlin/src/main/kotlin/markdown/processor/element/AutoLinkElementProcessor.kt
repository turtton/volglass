package markdown.processor.element

import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.entities.EntityConverter
import org.intellij.markdown.html.makeXssSafeDestination
import org.intellij.markdown.parser.LinkMap
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.AnchorHTMLAttributes
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.a

/**
 * Related [CommonMarkFlavourDescriptor]:L92-108(MarkdownElementTypes.AUTOLINK)
 */
class AutoLinkElementProcessor<Parent>(private val useSafeLinks: Boolean = true) : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val linkText = node.getTextInNode(markdownText)
        val linkLabel = EntityConverter.replaceEntities(
            linkText.subSequence(1, linkText.length - 1),
            processEntities = true,
            processEscapes = false,
        )
        val linkDestination = LinkMap.normalizeDestination(linkText, false).let {
            if (useSafeLinks) makeXssSafeDestination(it) else it
        }
        visitor.consumeTagOpen(node, a.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
        visitor.consume {
            this.unsafeCast<AnchorHTMLAttributes<HTMLAnchorElement>>().href = linkDestination.toString()
            +linkLabel
        }
        visitor.consumeTagClose(a.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
