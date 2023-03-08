package markdown.processor.element

import markdown.processor.NodeProcessor
import markdown.processor.TransparentInlineHolderNodeProcessor
import markdown.processor.TrimmingInlineHolderNodeProcessor
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.w3c.dom.HTMLElement
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.blockquote
import react.dom.html.ReactHTML.body
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.h5
import react.dom.html.ReactHTML.h6
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
        MarkdownElementTypes.LIST_ITEM to ListItemElementProcessor(),

        MarkdownTokenTypes.SETEXT_CONTENT to TrimmingInlineHolderNodeProcessor(),
        MarkdownElementTypes.SETEXT_1 to SimpleElementNodeProcessor(h1),
        MarkdownElementTypes.SETEXT_2 to SimpleElementNodeProcessor(h2),

        MarkdownTokenTypes.ATX_CONTENT to TrimmingInlineHolderNodeProcessor(),
        MarkdownElementTypes.ATX_1 to SimpleElementNodeProcessor(h1),
        MarkdownElementTypes.ATX_2 to SimpleElementNodeProcessor(h2),
        MarkdownElementTypes.ATX_3 to SimpleElementNodeProcessor(h3),
        MarkdownElementTypes.ATX_4 to SimpleElementNodeProcessor(h4),
        MarkdownElementTypes.ATX_5 to SimpleElementNodeProcessor(h5),
        MarkdownElementTypes.ATX_6 to SimpleElementNodeProcessor(h6),

        MarkdownElementTypes.AUTOLINK to AutoLinkElementProcessor(),

        MarkdownElementTypes.LINK_LABEL to TransparentInlineHolderNodeProcessor(),
        MarkdownElementTypes.LINK_TEXT to TransparentInlineHolderNodeProcessor(),
        MarkdownElementTypes.LINK_TITLE to TransparentInlineHolderNodeProcessor(),
    )
