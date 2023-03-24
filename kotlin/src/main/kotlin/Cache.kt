@file:OptIn(ExperimentalJsExport::class)

import csstype.ClassName
import external.CodeEncoder
import external.NextRouter
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.storeOf
import kotlin.js.Promise
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.promise
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import markdown.convertMarkdownToReactElement
import react.FC
import react.Props
import react.dom.html.ReactHTML.img

val cacheScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

val json = Json

// CacheData + DirectoryTreeData
val cacheData: KStore<Pair<CacheData, String>> = storeOf("backend.cache")

@Serializable
data class CacheData(val dependencyData: DependencyData, val fileNameInfo: FileNameInfo)

@JsExport
fun initCache(
    directoryTreeData: String,
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

    val dependencyData = DependencyData()
    val fileNameInfo = FileNameInfo(postFolder, duplicatedFile, fileNameToPath, fileNameToSlug, fileNameToMediaSlug)
    getAllFiles().forEach { filePath ->
        val content = readContent(filePath)
        if (filePath.contains("\\.md$".toRegex())) {
            // Analyze Dependencies
            convertMarkdownToReactElement(PathString(filePath).toFileName(postFolder, duplicatedFile), content, dependencyData, fileNameInfo, null, null)
        }
    }

    cacheData.set(CacheData(dependencyData, fileNameInfo) to directoryTreeData)
    return@promise fileNameToSlug.values.map { it.slug }.toTypedArray()
}

@JsExport
fun getCacheData(): Promise<Array<String>> = cacheScope.promise {
    val (cacheData, directoryTreeData) = cacheData.get()!!
    arrayOf(serialize(cacheData), directoryTreeData)
}

@JsExport
fun getContent(fileNameString: String, content: String, cacheData: String, router: NextRouter, codeEncoder: CodeEncoder): FC<Props> {
    val (dependingLinks, fileNameInfo) = deserialize<CacheData>(cacheData)
    val fileName = FileNameString(fileNameString)
    return if (fileName.isImageFile) {
        FC {
            img {
                // FIXME: Not work. Images are always floated to the left of its view.
                className = ClassName("float-none object-none object-center")
                src = fileNameInfo.fileNameToMediaSlug[fileName]!!.slug
                alt = fileName.fileName
            }
        }
    } else {
        convertMarkdownToReactElement(fileName, content, dependingLinks, fileNameInfo, router, codeEncoder)
    }
}

inline fun <reified T : @Serializable Any> deserialize(jsonString: String): T = json.decodeFromString(jsonString)

inline fun <reified T : @Serializable Any> serialize(obj: T): String = json.encodeToString(obj)
