package markdown.processor.element

import csstype.ClassName
import external.CodeEncoder
import kotlinx.js.jso
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
class CodeFenceElementProcessor<Parent>(private val encoder: CodeEncoder?) :
    NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val indentBefore = node.getTextInNode(markdownText).commonPrefixWith(" ".repeat(10)).length
        visitor.consumeTagOpen(node, pre.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())

        var childrenToConsider = node.children
        if (childrenToConsider.last().type == MarkdownTokenTypes.CODE_FENCE_END) {
            childrenToConsider = childrenToConsider.subList(0, childrenToConsider.size - 1)
        }

        var lastChildWasContent = false
        val codes = mutableListOf<String>()
        var language = ""

        childrenToConsider.forEach { child ->
            if (child.type in listOf(MarkdownTokenTypes.CODE_FENCE_CONTENT, MarkdownTokenTypes.EOL)) {
                val code = HtmlGenerator.trimIndents(HtmlGenerator.leafText(markdownText, child, false), indentBefore)
                codes += code.toString()
                lastChildWasContent = child.type == MarkdownTokenTypes.CODE_FENCE_CONTENT
            }
            if (child.type == MarkdownTokenTypes.FENCE_LANG) {
                language = HtmlGenerator.leafText(markdownText, child).toString().trim().split(' ')[0]
            }
        }
        if (lastChildWasContent) {
            codes += "\n"
        }
        val html = encoder?.invoke(codes.joinToString(separator = "").removeSurrounding("\n"), language)
            ?.split('\n')
            ?.joinToString(separator = "\n") { "<span class=\"code-line\">$it</span>" }
        visitor.consume {
            if (html != null && language.isNotEmpty()) {
                className = ClassName("language-$language")
            }
            code {
                if (language.isNotEmpty()) {
                    className = ClassName("language-$language")
                }
                if (html != null) {
                    dangerouslySetInnerHTML = jso {
                        __html = html
                    }
                } else {
                    codes.removeFirstOrNull()
                    codes.forEach {
                        +it
                    }
                }
            }
        }
        visitor.consumeTagClose(pre.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
