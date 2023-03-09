package markdown.processor.element

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.InlineLinkGeneratingProvider
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.dom.html.HTMLAttributes

/**
 * Related [InlineLinkGeneratingProvider]
 */
class InlineLinkElementProcessor<Parent>(
    baseURI: URI?,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, resolveAnchors)
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo? {
        val label = node.findChildOfType(MarkdownElementTypes.LINK_TEXT) ?: return null
        return LinkGeneratingProvider.RenderInfo(
            label,
            node.findChildOfType(MarkdownElementTypes.LINK_DESTINATION)?.getTextInNode(markdownText)?.let {
                LinkMap.normalizeDestination(it, true)
            } ?: "",
            node.findChildOfType(MarkdownElementTypes.LINK_TITLE)?.getTextInNode(markdownText)?.let {
                LinkMap.normalizeTitle(it)
            },
        )
    }
}
