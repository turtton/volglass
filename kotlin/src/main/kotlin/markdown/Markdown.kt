@file:OptIn(ExperimentalJsExport::class)

package markdown

import markdown.processor.element.createReactElementGeneratingProcessors
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import react.FC
import react.Props

@JsExport
fun convertMarkdownToReactElement(content: String): FC<Props> {
    val flavour = ObsidianMarkFlavourDescriptor()
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
    return ReactElementGenerator(content, parsedTree, createReactElementGeneratingProcessors(LinkMap(hashMapOf()), null)).generateElement()
}

@JsExport
fun convertMarkdownToHtml(content: String): String {
    val flavour = CommonMarkFlavourDescriptor()
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
    return HtmlGenerator(content, parsedTree, flavour).generateHtml()
}
