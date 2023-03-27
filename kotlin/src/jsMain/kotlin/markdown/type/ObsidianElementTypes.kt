package markdown.type

import org.intellij.markdown.IElementType

object ObsidianElementTypes {
    val LINK: IElementType = ObsidianElementType("LINK")
    val EMBED_LINK: IElementType = ObsidianElementType("EMBED_LINK")
    val TEX: IElementType = ObsidianElementType("TEX")
}
