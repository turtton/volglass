package markdown.processor

import markdown.TagConsumer
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLOListElement
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.OlHTMLAttributes
import react.dom.html.ReactHTML.ol

/**
 * [CommonMarkFlavourDescriptor]:L63-76(MarkdownElementTypes.ORDERED_LIST)
 */
class OrderedListElementProcessor<Parent : HTMLAttributes<HTMLElement>> : SimpleElementNodeProcessor<Parent, HTMLOListElement, OlHTMLAttributes<HTMLOListElement>, IntrinsicType<OlHTMLAttributes<HTMLOListElement>>>(ol) {
    override fun openTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, markdownText: String, node: ASTNode) {
        val attribute: (OlHTMLAttributes<HTMLOListElement>.() -> Unit)? = node.findChildOfType(MarkdownElementTypes.LIST_ITEM)
            ?.findChildOfType(MarkdownTokenTypes.LIST_NUMBER)
            ?.getTextInNode(markdownText)
            ?.toString()
            ?.trim()
            ?.let {
                val number = it.substring(0, it.length - 1).trimStart('0')
                if (number != "1") {
                    {
                        start = number.ifEmpty { "0" }.toInt()
                    }
                } else {
                    null
                }
            }
        visitor.consumeTagOpen(node, ol.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
        if (attribute != null) {
            visitor.consume {
                (this.unsafeCast<OlHTMLAttributes<HTMLOListElement>>()).attribute()
            }
        }
    }
}
