package markdown

import org.intellij.markdown.ast.ASTNode

interface LeafVisitor {
    fun visitLeaf(node: ASTNode)
}
