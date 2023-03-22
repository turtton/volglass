package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.dom.html.HTMLAttributes

class ObsidianLinkElementProcessor<Parent>(
    baseURI: URI?,
    router: dynamic,
    fileName: FileNameString,
    dependencyData: DependencyData,
    fileNameInfo: FileNameInfo,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors)
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo {
        // listOf(link) or listOf(link, |, title)
        val textNodes = node.children.filter { it.type == MarkdownTokenTypes.TEXT }
        val destinationTextNode = textNodes.first()
        val titleTextNode = if (textNodes.size > 1) textNodes.last() else destinationTextNode

        val destination = destinationTextNode.getTextInNode(markdownText)
        val title = titleTextNode.getTextInNode(markdownText)

        val lbracketNode = node.children[0]
        val rbracketNode = node.children.last()
        val slimNode = object : ASTNode by node {
            // listOf([, [, link, ], ]) -> listOf([, link, ])
            // listOf([, [, link, title, ], ]) -> listOf([, title, ])
            override val children: List<ASTNode> = listOf(lbracketNode, titleTextNode, rbracketNode)
        }
        return LinkGeneratingProvider.RenderInfo(
            slimNode,
            destination,
            title,
        )
    }
}
