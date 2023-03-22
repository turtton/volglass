package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.ReferenceLinksGeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.html.entities.EntityConverter
import org.intellij.markdown.parser.LinkMap
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.dom.html.HTMLAttributes

/**
 * Related [ReferenceLinksGeneratingProvider]
 */
class ReferenceLinksElementProcessor<Parent>(
    private val linkMap: LinkMap,
    baseURI: URI?,
    router: dynamic,
    fileName: FileNameString,
    dependencyData: DependencyData,
    fileNameInfo: FileNameInfo,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors)
    where Parent : HTMLAttributes<HTMLElement>,
          Parent : ChildrenBuilder {
    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo? {
        val label = node.children.firstOrNull { it.type == MarkdownElementTypes.LINK_LABEL } ?: return null
        val linkInfo = linkMap.getLinkInfo(label.getTextInNode(markdownText)) ?: return null
        val linkTextNode = node.children.firstOrNull { it.type == MarkdownElementTypes.LINK_TEXT }
        return LinkGeneratingProvider.RenderInfo(
            linkTextNode ?: label,
            EntityConverter.replaceEntities(linkInfo.destination, processEntities = true, processEscapes = true),
            linkInfo.title?.let { EntityConverter.replaceEntities(it, processEntities = true, processEscapes = true) },
        )
    }
}
