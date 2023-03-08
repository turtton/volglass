package markdown

import org.intellij.markdown.IElementType
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.GeneratingProvider
import org.intellij.markdown.html.URI
import org.intellij.markdown.parser.LinkMap

class ObsidianMarkFlavourDescriptor(
    useSafeLinks: Boolean = true,
    absolutizeAnchorLinks: Boolean = false,
) : CommonMarkFlavourDescriptor(useSafeLinks, absolutizeAnchorLinks) {
    @Deprecated("Use createReactElementGeneratingProcessors()", ReplaceWith("createReactElementGeneratingProcessors()"))
    override fun createHtmlGeneratingProviders(linkMap: LinkMap, baseURI: URI?): Map<IElementType, GeneratingProvider> = emptyMap()
}
