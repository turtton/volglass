package markdown.processor.element

import markdown.TagConsumer
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.ImageGeneratingProvider
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ImgHTMLAttributes
import react.dom.html.ReactHTML.img

/**
 * Related [ImageGeneratingProvider]
 */
class ImageElementProcessor<Parent>(linkMap: LinkMap, baseURI: URI?) : LinkElementProcessor<Parent>(baseURI) where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    protected val referenceLinkProcessor = ReferenceLinksElementProcessor<Parent>(linkMap, baseURI)
    protected val inlineLinkProcessor = InlineLinkElementProcessor<Parent>(baseURI)

    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo? {
        val inlineLinkNode = node.findChildOfType(MarkdownElementTypes.INLINE_LINK)
        if (inlineLinkNode != null) {
            return inlineLinkProcessor.getRenderInfo(markdownText, inlineLinkNode)
        }
        val referenceLinkNode = node.findChildOfType(MarkdownElementTypes.FULL_REFERENCE_LINK)
            ?: node.findChildOfType(MarkdownElementTypes.SHORT_REFERENCE_LINK)
        return referenceLinkNode?.let {
            referenceLinkProcessor.getRenderInfo(markdownText, it)
        }
    }

    override fun <Visitor> renderLink(visitor: Visitor, markdownText: String, node: ASTNode, info: LinkGeneratingProvider.RenderInfo) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor {
        visitor.consumeTagOpen(node, img.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
        visitor.consume {
            this.unsafeCast<ImgHTMLAttributes<HTMLImageElement>>().apply {
                src = makeAbsoluteUrl(info.destination)
                alt = getPlainTextFrom(info.label, markdownText)
                title = info.title?.toString()
            }
        }
        visitor.consumeTagClose(img.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }

    private fun getPlainTextFrom(node: ASTNode, markdownText: String): String {
        return ImageGeneratingProvider.REGEX.replace(node.getTextInNode(markdownText), "")
    }
}
