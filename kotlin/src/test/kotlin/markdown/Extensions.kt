package markdown

import kotlin.js.Json
import kotlin.test.assertEquals
import markdown.processor.element.createReactElementGeneratingProcessors
import mysticfall.kotlin.react.test.ReactTestSupport
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import react.ChildrenBuilder
import react.dom.html.ReactHTML.div

private val descriptor = ObsidianMarkFlavourDescriptor()

private val parser = MarkdownParser(descriptor)
fun Json.serialize(): String = JSON.stringify(this)
fun ReactTestSupport.doTest(markdown: String, expect: ChildrenBuilder.() -> Unit) {
    val markdownTree = parser.buildMarkdownTreeFromString(markdown)
    val element = ReactElementGenerator(markdown, markdownTree, createReactElementGeneratingProcessors(LinkMap.buildLinkMap(markdownTree, markdown), null, "")).generateElement()
    val actualRender = render { element() }
    val expectRender = render { div(expect) }
    val expectJson = expectRender.toJSON().serialize()
    val actualJson = actualRender.toJSON().serialize()
    assertEquals(expectJson, actualJson, "\nEx: $expectJson\nAc: $actualJson\n")
}
