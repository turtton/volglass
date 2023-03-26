package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import external.NextRouter
import markdown.LeafVisitor
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
class ImageElementProcessor<Parent>(
    linkMap: LinkMap,
    baseURI: URI?,
    router: NextRouter?,
    fileName: FileNameString,
    dependencyData: DependencyData,
    fileNameInfo: FileNameInfo,
) : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo)
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    protected val referenceLinkProcessor = ReferenceLinksElementProcessor<Parent>(linkMap, baseURI, router, fileName, dependencyData, fileNameInfo)
    protected val inlineLinkProcessor = InlineLinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo)

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

    override fun <Visitor> renderLink(visitor: Visitor, markdownText: String, node: ASTNode, info: LinkGeneratingProvider.RenderInfo) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        visitor.consumeTagOpen(node, img.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())

        val url = when (val resolvedDestination = resolveUrl(info.destination)) {
            is Destination.Router -> resolvedDestination.slug.slug
            is Destination.RawLink -> resolvedDestination.link.toString()
        }
        val label = getPlainTextFrom(info.label, markdownText)
        val title = info.title?.toString()
        visitor.consume {
            this.unsafeCast<ImgHTMLAttributes<HTMLImageElement>>().also {
                it.src = url
                it.alt = label
                if (title != null) {
                    it.title = title
                }
            }
        }
        visitor.consumeTagClose(img.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }

    private fun getPlainTextFrom(node: ASTNode, markdownText: String): String {
        return ImageGeneratingProvider.REGEX.replace(node.getTextInNode(markdownText), "")
    }
}
