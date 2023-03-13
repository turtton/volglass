package markdown.processor.element

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
import react.dom.events.MouseEventHandler
import react.dom.html.AnchorHTMLAttributes
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.a

/**
 * Related [LinkGeneratingProvider]
 */
abstract class LinkElementProcessor<Parent>(
    val baseURI: URI?,
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
                onClick = destination.routerAction
            }

            is Destination.RawLink -> visitor.consume {
                this.unsafeCast<AnchorHTMLAttributes<HTMLAnchorElement>>().href = destination.link.toString()
            }
        }
        visitor.consume {
            title = info.title?.toString()
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
    return object : LinkElementProcessor<Parent>(baseURI, resolveAnchors) {
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
    value class Router(val routerAction: MouseEventHandler<HTMLElement>) : Destination
    value class RawLink(val link: CharSequence) : Destination
}
