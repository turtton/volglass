package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import SlugString
import external.NextRouter
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import markdown.processor.TransparentInlineHolderNodeProcessor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.html.makeXssSafeDestination
import org.intellij.markdown.html.resolveToStringSafe
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.AnchorHTMLAttributes
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.a
import removeMdExtension

/**
 * Related [LinkGeneratingProvider]
 */
abstract class LinkElementProcessor<Parent>(
    val baseURI: URI?,
    val router: NextRouter?,
    val fileName: FileNameString,
    val dependencyData: DependencyData,
    val fileNameInfo: FileNameInfo,
    val resolveAnchors: Boolean = false,
) : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    abstract fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo?

    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val info = getRenderInfo(markdownText, node)
            ?: return fallBackProvider.processNode(visitor, markdownText, node)
        renderLink(visitor, markdownText, node, info)
    }

    protected fun resolveUrl(destination: CharSequence): Destination {
        if (!resolveAnchors && destination.startsWith('#')) {
            return Destination.RawLink(destination)
        }
        // TODO Check destination equals slug or not
        val slug = fileNameInfo.fileNameToSlug[FileNameString(destination.toString().removeMdExtension())]
        if (slug != null) {
            val targetFile = slug.toFileName(fileNameInfo.duplicatedFile)
            dependencyData.dependingLinks.getOrPut(fileName) { mutableListOf() }.add(targetFile)
            dependencyData.linkDependencies.getOrPut(targetFile) { mutableListOf() }.add(fileName)

            return Destination.Router(slug)
        }

        return baseURI?.resolveToStringSafe(destination.toString())
            ?.let { Destination.RawLink(it) }
            ?: Destination.RawLink(destination)
    }

    protected fun makeAbsoluteUrl(destination: CharSequence): String {
        if (!resolveAnchors && destination.startsWith('#')) {
            return destination.toString()
        }
        return baseURI?.resolveToStringSafe(destination.toString()) ?: destination.toString()
    }

    open fun <Visitor> renderLink(visitor: Visitor, markdownText: String, node: ASTNode, info: LinkGeneratingProvider.RenderInfo) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        visitor.consumeTagOpen(node, a.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
        when (val destination = resolveUrl(info.destination)) {
            is Destination.Router -> visitor.consume {
                onClick = {
                    router?.push(destination.slug.slug)
                }
            }

            is Destination.RawLink -> visitor.consume {
                this.unsafeCast<AnchorHTMLAttributes<HTMLAnchorElement>>().href = destination.link.toString()
            }
        }
        val linkTitle = info.title
        if (linkTitle != null) {
            visitor.consume {
                title = info.title?.toString()
            }
        }
        labelProvider.processNode(visitor, markdownText, info.label)
        visitor.consumeTagClose(a.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }

    companion object {
        val fallBackProvider =
            TransparentInlineHolderNodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>()

        val labelProvider =
            TransparentInlineHolderNodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>(1, -1)
    }
}

fun <Parent> LinkElementProcessor<Parent>.makeXssSafe(useSafeLinks: Boolean = true): LinkElementProcessor<Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    if (!useSafeLinks) return this
    return object : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors) {
        override fun <Visitor> renderLink(visitor: Visitor, markdownText: String, node: ASTNode, info: LinkGeneratingProvider.RenderInfo) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
            this@makeXssSafe.renderLink(visitor, markdownText, node, info)
        }

        override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo? {
            return this@makeXssSafe.getRenderInfo(markdownText, node)?.let {
                it.copy(destination = makeXssSafeDestination(it.destination))
            }
        }
    }
}

sealed interface Destination {
    value class Router(val slug: SlugString) : Destination
    value class RawLink(val link: CharSequence) : Destination
}
