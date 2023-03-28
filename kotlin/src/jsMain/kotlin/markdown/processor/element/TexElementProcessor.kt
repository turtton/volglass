package markdown.processor.element

import csstype.ClassName
import external.TexRender
import js.core.jso
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.processor.NodeProcessor
import markdown.type.ObsidianTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.p
import web.html.HTMLElement

class TexElementProcessor<Parent>(private val renderTex: TexRender?) : NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    override fun <Visitor> processNode(visitor: Visitor, markdownText: String, node: ASTNode) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val tex = node.children
            .filter { it.type != ObsidianTokenTypes.DOLLAR }
            .joinToString("") { it.getTextInNode(markdownText).toString() }
        // println("tex:$tex")
        val html = renderTex?.invoke(tex)
        visitor.consume {
            if (html != null) {
                p {
                    className = ClassName("inline")
                    dangerouslySetInnerHTML = jso {
                        __html = html
                    }
                }
            } else {
                +tex
            }
        }
    }
}
