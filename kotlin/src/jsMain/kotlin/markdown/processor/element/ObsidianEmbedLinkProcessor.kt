package markdown.processor.element

import DependencyData
import FileNameInfo
import FileNameString
import SlugString
import embedTargets
import external.CodeEncoder
import external.MermaidRender
import external.NextRouter
import external.TexRender
import markdown.LeafVisitor
import markdown.TagConsumer
import markdown.convertMarkdownToReactElement
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.html.LinkGeneratingProvider
import org.intellij.markdown.html.URI
import react.ChildrenBuilder
import react.IntrinsicType
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.audio
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.strong
import web.cssom.ClassName
import web.html.HTMLElement

/**
 * See [Official Help about EmbeddingLink](https://help.obsidian.md/Linking+notes+and+files/Embedding+files)
 */
class ObsidianEmbedLinkProcessor<Parent>(
    baseURI: URI?,
    router: NextRouter?,
    fileName: FileNameString,
    dependencyData: DependencyData,
    fileNameInfo: FileNameInfo,
    private val codeEncoder: CodeEncoder?,
    private val mermaidRender: MermaidRender?,
    private val texRender: TexRender?,
    resolveAnchors: Boolean = false,
) : LinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors)
    where Parent : HTMLAttributes<HTMLElement>, Parent : ChildrenBuilder {
    private val obsidianLinkProcessor = ObsidianLinkElementProcessor<Parent>(baseURI, router, fileName, dependencyData, fileNameInfo, resolveAnchors)
    override fun getRenderInfo(markdownText: String, node: ASTNode): LinkGeneratingProvider.RenderInfo {
        return obsidianLinkProcessor.getRenderInfo(markdownText, node)
    }

    override fun <Visitor> renderLink(visitor: Visitor, markdownText: String, node: ASTNode, info: LinkGeneratingProvider.RenderInfo) where Visitor : TagConsumer<IntrinsicType<HTMLAttributes<HTMLElement>>, Parent>, Visitor : org.intellij.markdown.ast.visitors.Visitor, Visitor : LeafVisitor {
        val destination = info.destination.toString()
        // [internalDestination] specifies the display range for markdown and pdf
        val (rawUrl, internalDestination) = destination.split('#').let { it.first() to it.getOrNull(1) }
        // [url] may point to media content in the public folder
        val url = when (val resolvedDestination = resolveUrl(rawUrl)) {
            is Destination.Router -> resolvedDestination.slug.slug
            is Destination.RawLink -> {
                println("Warn: Failed to get slug data. target:$rawUrl")
                "/${resolvedDestination.link}"
            }
        }
        val sizeOption = info.title
        // for image link option
        val (imageWidth, imageHeight) = if (sizeOption != null && destination != sizeOption) {
            sizeOption.split('x').let { it.first().toDouble() to it.getOrNull(1)?.toDouble() }
        } else {
            null to null
        }
        val targetFile = FileNameString(destination)
        when {
            targetFile.isImageFile -> visitor.consume {
                img {
                    src = url
                    alt = destination
                    if (imageWidth != null) {
                        width = imageWidth
                    }
                    if (imageWidth != null) {
                        height = imageHeight
                    }
                }
            }
            targetFile.isSoundFile -> visitor.consume {
                audio {
                    controls = true
                    controlsList = "nodownload"
                    src = url
                }
            }
            // markdown file
            // TODO support internalDestination
            else -> {
                val targetFileName = SlugString(url).toFileName(fileNameInfo.duplicatedFile)
                if (targetFileName == fileName) {
                    println("Warning: Embed loop is detected in ${fileName.fileName}")
                    return
                }
                if (embedTargets != null) {
                    embedTargets?.add(targetFile)
                } else {
                    val content = dependencyData.embedContents[targetFileName] ?: ""
                    val element = convertMarkdownToReactElement(targetFileName, content, dependencyData, fileNameInfo, router, codeEncoder, mermaidRender, texRender)
                    visitor.consume {
                        div {
                            className = ClassName("border-l-2 px-4 border-gray-300 dark:border-gray-600")
                            strong {
                                +targetFileName.fileName
                            }
                            element()
                        }
                    }
                }
            }
        }
    }
}
