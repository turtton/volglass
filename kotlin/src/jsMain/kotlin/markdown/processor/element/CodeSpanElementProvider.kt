package markdown.processor.element

import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.CodeSpanGeneratingProvider
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.code
import web.html.HTMLElement

/**
 * Related [CodeSpanGeneratingProvider]
 */
class CodeSpanElementProvider<Parent> :
    NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val nodes = node.children.subList(1, node.children.size - 1)
        val output = nodes.joinToString(separator = "") {
            // HtmlGenerator.leafText(markdownText, it, false)
            it.getTextInNode(markdownText)
        }.trim()
        visitor.consumeTagOpen(node, code)
        visitor.consume {
            // TODO Check behavior
            +output
        }
        visitor.consumeTagClose(code)
    }
}
