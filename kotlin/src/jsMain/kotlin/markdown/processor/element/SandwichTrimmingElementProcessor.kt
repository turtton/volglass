package markdown.processor.element

import markdown.TagConsumer
import markdown.processor.TrimmingInlineHolderNodeProcessor
import org.intellij.markdown.ast.ASTNode
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import web.html.HTMLElement

class SandwichTrimmingElementProcessor<
    Element : HTMLElement,
    Attribute : HTMLAttributes<Element>,
    Tag : IntrinsicType<Attribute>,
    >(
    val tag: Tag,
) : TrimmingInlineHolderNodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>() {
    override fun openTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>, markdownText: String, node: ASTNode) {
        visitor.consumeTagOpen(node, tag.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }

    override fun closeTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, HTMLAttributes<HTMLElement>>, markdownText: String, node: ASTNode) {
        visitor.consumeTagClose(tag.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
