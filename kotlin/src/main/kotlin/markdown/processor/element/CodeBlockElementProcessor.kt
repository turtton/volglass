package markdown.processor.element

import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.pre

/**
 * Related [CommonMarkFlavourDescriptor]:L132-149(MarkdownElementTypes.CODE_BLOCK)
 */
class CodeBlockElementProcessor<Parent> : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        visitor.consumeTagOpen(node, pre.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
        visitor.consumeTagOpen(node, code)

        node.children.forEach { child ->
            if (child.type == MarkdownTokenTypes.CODE_LINE) {
                visitor.consume {
                    +HtmlGenerator.trimIndents(HtmlGenerator.leafText(markdownText, child, false), 4).toString()
                }
            } else if (child.type == MarkdownTokenTypes.EOL) {
                visitor.consume {
                    +"\n"
                }
            }
        }

        visitor.consume {
            +"\n"
        }
        visitor.consumeTagClose(code)
        visitor.consumeTagClose(pre.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
