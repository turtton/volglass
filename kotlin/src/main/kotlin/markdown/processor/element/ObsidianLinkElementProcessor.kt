package markdown.processor.element

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.dom.html.HTMLAttributes

class ObsidianLinkElementProcessor<Parent>(
    baseURI: URI?,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, resolveAnchors)
where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo {
        @Suppress("RegExpRedundantEscape")
        val linkText = node.getTextInNode(markdownText)
            .replace("\\[".toRegex(), "")
            .replace("\\]".toRegex(), "")
        val separated = linkText.split('|')
        val destination = separated[0]
        val title = if (separated.size == 2) separated[1] else destination
        val slimNode = object : ASTNode by node {
            // listOf([, [, link, ], ]) -> listOf([, link, ])
            override val children: List<ASTNode> = node.children.subList(1, node.children.size - 1)
        }
        return LinkGeneratingProvider.RenderInfo(
            slimNode,
            destination,
            title,
        )
    }
}
