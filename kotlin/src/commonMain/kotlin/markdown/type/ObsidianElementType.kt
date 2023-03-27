package markdown.type

import org.intellij.markdown.MarkdownElementType

open class ObsidianElementType(name: String, isToken: Boolean = false) : MarkdownElementType(name, isToken) {
    override fun toString(): String {
        return super.toString().replace("Markdown", "Obsidian")
    }
}
