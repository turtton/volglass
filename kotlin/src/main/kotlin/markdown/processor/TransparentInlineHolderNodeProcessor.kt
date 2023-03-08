package markdown.processor

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.TransparentInlineHolderProvider

/**
 * Related [TransparentInlineHolderProvider]
 */
class TransparentInlineHolderNodeProcessor<Type, Parent>(private val renderFrom: Int = 0, private val renderTo: Int = 0) : SingleParagraphNodeProvider<Type, Parent>() {
    override fun childrenToRender(node: ASTNode): List<ASTNode> {
        return node.children.subList(renderFrom, node.children.size + renderTo)
    }
}