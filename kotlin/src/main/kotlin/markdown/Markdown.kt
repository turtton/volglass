@file:OptIn(ExperimentalJsExport::class)

package markdown

import DependencyData
import FileNameInfo
import FileNameString
import RoutableProps
import markdown.processor.element.createReactElementGeneratingProcessors
import org.intellij.markdown.parser.LinkMap
import org.intellij.markdown.parser.MarkdownParser
import react.FC

fun convertMarkdownToReactElement(fileName: FileNameString, content: String, dependencyData: DependencyData, fileNameInfo: FileNameInfo): FC<RoutableProps> {
    val flavour = ObsidianMarkFlavourDescriptor()
    val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(content)
    return ReactElementGenerator(content, parsedTree, createReactElementGeneratingProcessors(LinkMap.buildLinkMap(parsedTree, content), null, fileName, dependencyData, fileNameInfo)).generateElement()
}
