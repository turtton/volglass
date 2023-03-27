package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import external.NextRouter
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.InlineLinkGeneratingProvider
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap
import react.ChildrenBuilder
import react.dom.html.HTMLAttributes
import web.html.HTMLElement

/**
 * Related [InlineLinkGeneratingProvider]
 */
class InlineLinkElementProcessor<Parent>(
    baseURI: URI?,
    router: NextRouter?,
    fileName: FileNameString,
    dependencyData: DependencyData,
    fileNameInfo: FileNameInfo,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors)
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
