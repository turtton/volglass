package markdown.processor.element

import markdown.TagConsumer
import markdown.processor.SandwichNodeProcessor
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.SimpleTagProvider
import org.w3c.dom.HTMLElement
import react.IntrinsicType
import react.dom.html.HTMLAttributes

/**
 * Related [SimpleTagProvider]
 */
open class SimpleElementNodeProcessor<
    Parent : HTMLAttributes<HTMLElement>,
    Element : HTMLElement,
    Attribute : HTMLAttributes<Element>,
    Type : IntrinsicType<Attribute>,
    >(
    private val tag: Type,
) : SandwichNodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent> {
    override fun openTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, markdownText: String, node: ASTNode) {
        visitor.consumeTagOpen(node, tag.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }

    override fun closeTag(visitor: TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, markdownText: String, node: ASTNode) {
        visitor.consumeTagClose(tag.unsafeCast<IntrinsicType<HTMLAttributes<HTMLElement>>>())
    }
}
