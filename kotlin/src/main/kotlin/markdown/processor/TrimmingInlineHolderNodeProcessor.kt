package markdown.processor

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.TrimmingInlineHolderProvider

/**
 * Related [TrimmingInlineHolderProvider]
 */
class TrimmingInlineHolderNodeProcessor<Tag, Parent> : SingleParagraphNodeProvider<Tag, Parent>() {
    override fun childrenToRender(node: ASTNode): List<ASTNode> {
        val children = node.children
        val from = children.filter { it.type == MarkdownTokenTypes.WHITE_SPACE }.size
        var to = children.size
        while (to > from && children[to - 1].type == MarkdownTokenTypes.WHITE_SPACE) {
            to--
        }
        return children.subList(from, to)
    }
}
