package markdown.processor

import markdown.TagConsumer
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.html.InlineHolderGeneratingProvider

/**
 * Related [InlineHolderGeneratingProvider]
 */
abstract class InlineHolderNodeProcessor<Type, Parent> : SandwichNodeProcessor<Type, Parent> {
    open fun childrenToRender(node: ASTNode): List<ASTNode> = node.children

    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<Type, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor {
        openTag(visitor, markdownText, node)

        childrenToRender(node).forEach {
            if (it is LeafASTNode) {
                println("WARN: visitLeaf not implemented")
            } else {
                it.accept(visitor)
            }
        }

        closeTag(visitor, markdownText, node)
    }
}

open class SingleParagraphNodeProvider<Type, Parent> : InlineHolderNodeProcessor<Type, Parent>() {
    override fun openTag(visitor: TagConsumer<Type, Parent>, markdownText: String, node: ASTNode) {}
    override fun closeTag(visitor: TagConsumer<Type, Parent>, markdownText: String, node: ASTNode) {}
}
