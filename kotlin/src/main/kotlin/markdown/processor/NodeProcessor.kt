package markdown.processor

import markdown.TagConsumer
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren

interface NodeProcessor<out Tag, in Parent> {
    fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<Tag, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor
}

interface SandwichNodeProcessor<out Tag, in Parent> : NodeProcessor<Tag, Parent> {
    fun openTag(visitor: TagConsumer<Tag, Parent>, markdownText: String, node: ASTNode)
    fun closeTag(visitor: TagConsumer<Tag, Parent>, markdownText: String, node: ASTNode)

    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<Tag, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor {
        openTag(visitor, markdownText, node)
        node.acceptChildren(visitor)
        closeTag(visitor, markdownText, node)
    }
}
