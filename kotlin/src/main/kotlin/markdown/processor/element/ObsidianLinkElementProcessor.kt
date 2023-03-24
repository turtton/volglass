package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import external.NextRouter
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.dom.html.HTMLAttributes

class ObsidianLinkElementProcessor<Parent>(
    baseURI: URI?,
    router: NextRouter?,
    fileName: FileNameString,
    dependencyData: DependencyData,
    fileNameInfo: FileNameInfo,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors)
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo {
        // listOf(link) or listOf(link, |, title) or listOf(link|title)
        val textNodes = node.children.filter { it.type == MarkdownTokenTypes.TEXT }
        val destinationTextNode = textNodes.first()
        // link or title or link|title
        val titleTextNode = if (textNodes.size > 1) textNodes.last() else destinationTextNode

        // link or {link|title -> listOf(link, title) -> link}
        val destination = destinationTextNode.getTextInNode(markdownText).split('|').first()
        val rawTitle = titleTextNode.getTextInNode(markdownText)
        // title or {link|title -> listOf(link, title) -> title}
        val title = rawTitle.split('|').last()

        val lbracketNode = node.children[0]
        val rbracketNode = node.children.last()

        // link|>>t<<ext or 0
        val verticalLinePosition = rawTitle.indexOf('|') + 1
        val titleNode = LeafASTNode(titleTextNode.type, titleTextNode.startOffset + verticalLinePosition, titleTextNode.endOffset)
        val slimNode = object : ASTNode by node {
            // listOf([, [, link, ], ]) -> listOf([, link, ])
            // or listOf([, [, link, title, ], ]) -> listOf([, title, ])
            override val children: List<ASTNode> = listOf(lbracketNode, titleNode, rbracketNode)
        }
        return LinkGeneratingProvider.RenderInfo(
            slimNode,
            destination,
            title,
        )
    }
}
