package markdown.processor.element

import markdown.TagConsumer
import markdown.processor.TransparentInlineHolderNodeProcessor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.SimpleInlineTagProvider
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import web.html.HTMLElement

/**
 * Related [SimpleInlineTagProvider]
 */
class SimpleInlineElementProcessor<
    Element : HTMLElement,
    Attribute : HTMLAttributes<Element>,
    Tag : IntrinsicType<Attribute>,
    >(
    val tag: Tag,
    renderFrom: Int = 0,
    renderTo: Int = 0,
) : TransparentInlineHolderNodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>(renderFrom, renderTo) {
    override fun openTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>, markdownText: String, node: ASTNode) {
        visitor.consumeTagOpen(node, tag.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }

    override fun closeTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>, markdownText: String, node: ASTNode) {
        visitor.consumeTagClose(tag.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
