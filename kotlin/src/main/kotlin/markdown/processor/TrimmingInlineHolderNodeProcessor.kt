package markdown.processor

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.TrimmingInlineHolderProvider

/**
 * Related [TrimmingInlineHolderProvider]
 */
open class TrimmingInlineHolderNodeProcessor<Tag, Parent> : SilentParagraphNodeProcessor<Tag, Parent>() {
    override fun childrenToRender(node: ASTNode): List<ASTNode> {
        val children = node.children
        var from = 0
        while (from < children.size && children[from].type == MarkdownTokenTypes.WHITE_SPACE) {
            from++
        }
        var to = children.size
        while (to > from && children[to - 1].type == MarkdownTokenTypes.WHITE_SPACE) {
            to--
        }
        return children.subList(from, to)
    }
}
