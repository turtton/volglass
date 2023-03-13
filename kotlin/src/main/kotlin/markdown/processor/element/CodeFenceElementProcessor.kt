package markdown.processor.element

import csstype.ClassName
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.html.HtmlGenerator
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.pre

/**
 * Related [org.intellij.markdown.html.CodeFenceGeneratingProvider]
 */
@Suppress("KDocUnresolvedReference")
class CodeFenceElementProcessor<Parent> : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val indentBefore = node.getTextInNode(markdownText).commonPrefixWith(" ".repeat(10)).length
        visitor.consumeTagOpen(node, pre.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())

        var childrenToConsider = node.children
        if (childrenToConsider.last().type == MarkdownTokenTypes.CODE_FENCE_END) {
            childrenToConsider = childrenToConsider.subList(0, childrenToConsider.size - 1)
        }

        var isCodeTagOpened = false
        var lastChildWasContent = false

        val configurations = arrayListOf<Parent.() -> Unit>()
        childrenToConsider.forEach { child ->
            if (isCodeTagOpened && child.type in listOf(MarkdownTokenTypes.CODE_FENCE_CONTENT, MarkdownTokenTypes.EOL)) {
                visitor.consume {
                    +HtmlGenerator.trimIndents(HtmlGenerator.leafText(markdownText, child, false), indentBefore).toString()
                }
                lastChildWasContent = child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT
            }
            if (!isCodeTagOpened && child.type == MarkdownTokenTypes.FENCE_LANG) {
                configurations += {
                    className = ClassName("language-${HtmlGenerator.leafText(markdownText, child).toString().trim().split(' ')[0]}")
                }
            }
            if (!isCodeTagOpened && child.type == MarkdownTokenTypes.EOL) {
                visitor.consumeTagOpen(node, code)
                visitor.consume {
                    configurations.forEach { it() }
                }
                isCodeTagOpened = true
            }
        }
        if (!isCodeTagOpened) {
            visitor.consumeTagOpen(node, code)
            visitor.consume {
                configurations.forEach { it() }
            }
        }
        if (lastChildWasContent) {
            visitor.consume {
                +"\n"
            }
        }
        visitor.consumeTagClose(code)
        visitor.consumeTagClose(pre.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
