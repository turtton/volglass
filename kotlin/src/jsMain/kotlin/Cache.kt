@file:OptIn(ExperimentalJsExport::class)

import external.CanvasRender
import external.CodeEncoder
import external.MermaidRender
import external.NextRouter
import external.TexRender
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import kotlin.js.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.promise
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import markdown.convertMarkdownToReactElement
import okio.Path.Companion.toPath
import react.FC
import react.Props
import react.dom.html.ReactHTML.img
import web.cssom.ClassName

val cacheScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

val json = Json { ignoreUnknownKeys = true }

// only available on initializing cache
var embedTargets: MutableSet<FileNameString>? = null

val cacheData: KStore<VolglassCache> = storeOf("volglass.cache".toPath())

@Serializable
data class VolglassCache(val cacheData: CacheData, val directoryTreeData: String, val searchIndex: String)

@Serializable
data class CacheData(val dependencyData: DependencyData, val fileNameInfo: FileNameInfo)

@JsExport
fun initCache(
    directoryTreeData: String,
    searchIndex: String,
    // -> filePath
    getAllFiles: () -> Array<String>,
    // -> absolute path for markdown folder (`post directory`)
    getMarkdownFolder: () -> String,
    // -> absolute path for `public` directory
    getPublicFolder: () -> String,
    toSlug: (String) -> String,
    // target file path -> content
    readContent: (String) -> String,
    // target file path -> result path
    copyToPublicDir: (String) -> String,
): Promise<Array<String>> = cacheScope.promise {
    val fileNameToSlug = mutableMapOf<FileNameString, SlugString>()
    val fileNameToMediaSlug = mutableMapOf<FileNameString, SlugString>()
    val fileNameToPath = mutableMapOf<FileNameString, PathString>()
    val duplicatedFile = mutableSetOf<String>()
    val nameCache = mutableSetOf<String>()
    val postFolder = getMarkdownFolder()
    val publicFolder = getPublicFolder()
    getAllFiles().forEach { filePath ->
        if (filePath.contains(".gitkeep")) return@forEach
        var pathString = PathString(filePath)
        val plainFileName = pathString.toSlugString(postFolder).toPlainFileName()
        if (nameCache.contains(plainFileName.fileName)) {
            duplicatedFile += plainFileName.fileName
            val duplicatedPath = fileNameToPath.remove(plainFileName) ?: error("DuplicatedName:$plainFileName is not found in fileNameToPath")
            val duplicatedFileName = duplicatedPath.toFileName(postFolder, nameCache)
            fileNameToPath[duplicatedFileName] = duplicatedPath
            val duplicatedSlug = fileNameToSlug.remove(plainFileName)!!
            fileNameToSlug[duplicatedFileName] = duplicatedSlug
        }
        val fileName = pathString.toFileName(postFolder, nameCache)
        val slug = SlugString(toSlug(filePath))
        if (fileName.isMediaFile) {
            pathString = PathString(copyToPublicDir(filePath))
            fileNameToMediaSlug[fileName] = pathString.toSlugString(publicFolder)
        }
        fileNameToSlug[fileName] = slug
        fileNameToPath[fileName] = pathString

        nameCache += plainFileName.fileName
    }

    embedTargets = mutableSetOf()
    val dependencyData = DependencyData()
    val fileNameInfo = FileNameInfo(postFolder, duplicatedFile, fileNameToPath, fileNameToSlug, fileNameToMediaSlug)
    getAllFiles().forEach { filePath ->
        val fileName = PathString(filePath).toFileName(postFolder, duplicatedFile)

        if (fileName.isCanvasFile) {
            val canvasData = deserialize<CanvasData>(readContent(filePath))
            canvasData.nodes
                .mapNotNull { node -> (node.nodeData as? NodeFile)?.file }
                .forEach { file ->
                    val slugString = "/${file.replace(' ', '+').removeMdExtension()}"
                    val nodeFileName = SlugString(slugString).toFileName(duplicatedFile)
                    if (!nodeFileName.isMediaFile) {
                        embedTargets!!.add(nodeFileName)
                    }
                }
        } else if (!fileName.isMediaFile) {
            val content = readContent(filePath)
            // Analyze Dependencies
            convertMarkdownToReactElement(PathString(filePath).toFileName(postFolder, duplicatedFile), content, dependencyData, fileNameInfo, null, null, null, null)
        }
    }
    embedTargets!!.forEach {
        val path = fileNameToPath[it] ?: error("Failed to get path. Target:${it.fileName}")
        dependencyData.embedContents[it] = readContent(path.path)
    }

    cacheData.set(VolglassCache(CacheData(dependencyData, fileNameInfo), directoryTreeData, searchIndex))
    return@promise fileNameToSlug.values.map { it.slug }.toTypedArray()
}

@JsExport
fun getCacheData(): Promise<Array<String>> = cacheScope.promise {
    val (cacheData, directoryTreeData, searchIndex) = cacheData.get()!!
    arrayOf(serialize(cacheData), directoryTreeData, searchIndex)
}

@JsExport
fun getContent(fileNameString: String, content: String, cacheData: String, router: NextRouter, codeEncoder: CodeEncoder, mermaidRender: MermaidRender, texRender: TexRender, renderCanvas: CanvasRender): FC<Props> {
    val (dependingLinks, fileNameInfo) = deserialize<CacheData>(cacheData)
    val fileName = FileNameString(fileNameString)
    return when {
        fileName.isImageFile -> {
            FC {
                img {
                    className = ClassName("mx-auto")
                    src = fileNameInfo.fileNameToMediaSlug[fileName]!!.slug
                    alt = fileName.fileName
                }
            }
        }
        fileName.isCanvasFile -> {
            val canvasData = deserialize<CanvasData>(content)
            val contentGetter: (String) -> FC<Props> = {
                val contentFileName = SlugString(it.removeMdExtension()).toFileName(fileNameInfo.duplicatedFile)
                val embedContent = dependingLinks.embedContents[contentFileName] ?: ""
                getContent(contentFileName.fileName, embedContent, cacheData, router, codeEncoder, mermaidRender, texRender, renderCanvas)
            }
            renderCanvas(canvasData, contentGetter)
        }
        else -> {
            convertMarkdownToReactElement(fileName, content, dependingLinks, fileNameInfo, router, codeEncoder, mermaidRender, texRender)
        }
    }
}

inline fun <reified T : @Serializable Any> deserialize(jsonString: String): T = json.decodeFromString(jsonString)

inline fun <reified T : @Serializable Any> serialize(obj: T): String = json.encodeToString(obj)
