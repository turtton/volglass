package markdown.processor

import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.blockquote
import react.dom.html.ReactHTML.body
import react.dom.html.ReactHTML.ul

fun <Parent> createReactElementGeneratingProcessors(): Map<IElementType, NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder =
    mapOf(
        MarkdownElementTypes.MARKDOWN_FILE to SimpleElementNodeProcessor(body),
        MarkdownElementTypes.HTML_BLOCK to HtmlElementProcessor(),
        MarkdownTokenTypes.HTML_TAG to HtmlBlockElementProcessor(),
        MarkdownElementTypes.BLOCK_QUOTE to SimpleElementNodeProcessor(blockquote),
        MarkdownElementTypes.ORDERED_LIST to OrderedListElementProcessor(),
        MarkdownElementTypes.UNORDERED_LIST to SimpleElementNodeProcessor(ul),
    )
