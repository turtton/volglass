package markdown.processor

import markdown.LeafVisitor
import markdown.TagConsumer
import org.intellij.markdown.ast.ASTNode

class EmptyNodeProcessor<Tag, Parent> : NodeProcessor<Tag, Parent> {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<Tag, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {}
}
