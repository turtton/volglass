package markdown.processor

import markdown.LeafVisitor
import markdown.TagConsumer
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.visitors.Visitor as NodeVisitor

interface NodeProcessor<out Tag, in Parent> {
    fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<Tag, Parent>, Visitor : NodeVisitor, Visitor : LeafVisitor
}

interface SandwichNodeProcessor<out Tag, in Parent> : NodeProcessor<Tag, Parent> {
    fun openTag(visitor: TagConsumer<Tag, Parent>, markdownText: String, node: ASTNode)
    fun closeTag(visitor: TagConsumer<Tag, Parent>, markdownText: String, node: ASTNode)

    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<Tag, Parent>, Visitor : NodeVisitor, Visitor : LeafVisitor {
        openTag(visitor, markdownText, node)
        node.acceptChildren(visitor)
        closeTag(visitor, markdownText, node)
    }
}
