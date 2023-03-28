package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import external.CodeEncoder
import external.MermaidRender
import external.NextRouter
import external.TexRender
import markdown.processor.EmptyNodeProcessor
import markdown.processor.NodeProcessor
import markdown.processor.TransparentInlineHolderNodeProcessor
import markdown.processor.TrimmingInlineHolderNodeProcessor
import markdown.type.ObsidianElementTypes
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.blockquote
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.em
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.h5
import react.dom.html.ReactHTML.h6
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.strong
import react.dom.html.ReactHTML.ul
import web.html.HTMLElement

/**
 * Related [CommonMarkFlavourDescriptor.createHtmlGeneratingProviders]
 */
fun <Parent> createReactElementGeneratingProcessors(
    linkMap: LinkMap,
    baseURI: URI?,
    filename: FileNameString,
    router: NextRouter? = null,
    encoder: CodeEncoder? = null,
    mermaidRender: MermaidRender? = null,
    texRender: TexRender? = null,
    dependencyData: DependencyData = DependencyData(),
    fileNameInfo: FileNameInfo = FileNameInfo(),
    useSafeLinks: Boolean = true,
    absolutizeAnchorLinks: Boolean = false,
): Map<IElementType, NodeProcessor<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>>
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder =
    mapOf(
        ObsidianElementTypes.LINK to ObsidianLinkElementProcessor<Parent>(baseURI, router, filename, dependencyData, fileNameInfo, absolutizeAnchorLinks).makeXssSafe(useSafeLinks),
        ObsidianElementTypes.EMBED_LINK to ObsidianEmbedLinkProcessor<Parent>(baseURI, router, filename, dependencyData, fileNameInfo, encoder, mermaidRender, texRender, absolutizeAnchorLinks).makeXssSafe(useSafeLinks),
        ObsidianElementTypes.TEX to TexElementProcessor(texRender),

        MarkdownElementTypes.MARKDOWN_FILE to SimpleElementNodeProcessor(div),
        MarkdownElementTypes.HTML_BLOCK to HtmlBlockElementProcessor(),
        MarkdownTokenTypes.HTML_TAG to HtmlElementProcessor(),
        MarkdownElementTypes.BLOCK_QUOTE to SimpleElementNodeProcessor(blockquote),
        MarkdownElementTypes.ORDERED_LIST to OrderedListElementProcessor(),
        MarkdownElementTypes.UNORDERED_LIST to SimpleElementNodeProcessor(ul),
        MarkdownElementTypes.LIST_ITEM to ListItemElementProcessor(texRender),

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

        MarkdownElementTypes.INLINE_LINK to InlineLinkElementProcessor<Parent>(baseURI, router, filename, dependencyData, fileNameInfo, absolutizeAnchorLinks).makeXssSafe(useSafeLinks),

        MarkdownElementTypes.FULL_REFERENCE_LINK to ReferenceLinksElementProcessor<Parent>(linkMap, baseURI, router, filename, dependencyData, fileNameInfo, absolutizeAnchorLinks).makeXssSafe(useSafeLinks),
        MarkdownElementTypes.SHORT_REFERENCE_LINK to ReferenceLinksElementProcessor<Parent>(linkMap, baseURI, router, filename, dependencyData, fileNameInfo, absolutizeAnchorLinks).makeXssSafe(useSafeLinks),

        MarkdownElementTypes.IMAGE to ImageElementProcessor<Parent>(linkMap, baseURI, router, filename, dependencyData, fileNameInfo).makeXssSafe(useSafeLinks),

        MarkdownElementTypes.LINK_DEFINITION to EmptyNodeProcessor(),

        MarkdownElementTypes.CODE_FENCE to CodeFenceElementProcessor(encoder, mermaidRender),
        MarkdownElementTypes.CODE_BLOCK to CodeBlockElementProcessor(),

        MarkdownTokenTypes.HORIZONTAL_RULE to SingleElementProcessor(hr),
        MarkdownTokenTypes.HARD_LINE_BREAK to SingleElementProcessor(br),

        MarkdownElementTypes.PARAGRAPH to SandwichTrimmingElementProcessor(div),
        MarkdownElementTypes.EMPH to SimpleInlineElementProcessor(em, 1, -1),
        MarkdownElementTypes.STRONG to SimpleInlineElementProcessor(strong, 2, -2),
        MarkdownElementTypes.CODE_SPAN to CodeSpanElementProvider(),
    )
