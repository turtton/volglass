@file:OptIn(ExperimentalJsExport::class)

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

/**
 * Refers actual file
 * Examples: README.md, path/Content.md
 */

typealias PathString = String

/**
 * Refers web slug
 * Examples: /README, /path/Content
 */
typealias SlugString = String

/**
 * Includes extension expect markdown
 *
 * This may contain path if same name file exists.
 *
 * Related: [toFileName]
 */
typealias FileNameString = String

val backendCoroutine = CoroutineScope(Dispatchers.Default + SupervisorJob())

val json = Json

val cacheData: KStore<CacheData> = storeOf("backend.cache")

@JsExport
fun initCache(
    getAllFiles: () -> Array<String>,
    getMarkdownFolder: () -> String,
    toSlug: (String) -> String,
    readContent: (PathString) -> String,
): Promise<Array<String>> = backendCoroutine.promise {
    val fileNameToSlug = mutableMapOf<FileNameString, PathString>()
    val fileNameToPath = mutableMapOf<FileNameString, PathString>()
    val duplicatedFile = mutableSetOf<String>()
    val nameCache = arrayListOf<String>()
    val postFolder = getMarkdownFolder()
    getAllFiles().forEach { filePath ->
        val plainFileName = filePath.toPlainFileName()
        if (nameCache.contains(plainFileName)) {
            duplicatedFile += plainFileName
            val duplicatedPath = fileNameToPath.remove(plainFileName) ?: error("DuplicatedName:$plainFileName is not found in fileNameToPath")
            val duplicatedFileName = duplicatedPath.toFileName(postFolder, nameCache.toTypedArray())
            fileNameToPath[duplicatedFileName] = duplicatedPath
            val duplicatedSlug = fileNameToSlug.remove(plainFileName)!!
            fileNameToSlug[duplicatedFileName] = duplicatedSlug
        }
        val fileName = filePath.toFileName(postFolder, nameCache.toTypedArray())

        fileNameToPath[fileName] = filePath
        val slug = toSlug(filePath)
        fileNameToSlug[fileName] = slug

        nameCache += plainFileName
    }

    val dependencyData = DependencyData()
    val fileNameInfo = FileNameInfo(postFolder, duplicatedFile, fileNameToPath, fileNameToSlug)
    getAllFiles().forEach { filePath ->
        val content = readContent(filePath)
        // Analyze Dependencies
        convertMarkdownToReactElement(filePath.toFileName(postFolder, nameCache.toTypedArray()), content, dependencyData, fileNameInfo, null)
    }

    cacheData.set(CacheData(dependencyData, fileNameInfo))
    return@promise fileNameToSlug.values.toTypedArray()
}

@JsExport
fun PathString.toFileName(postFolder: String, duplicatedFile: Array<String>): FileNameString {
    return toFileName(postFolder, duplicatedFile.toSet())
}

fun PathString.toFileName(postFolder: String, duplicatedFile: Set<String>): FileNameString {
    val plainFileName = toPlainFileName()
    return if (!duplicatedFile.contains(plainFileName)) {
        plainFileName
    } else {
        // /path/to/post/dir/README.md -> /dir/README.md -> dir/README.md -> dir/README
        replace(postFolder, "").substring(1).removeMdExtension()
    }
}

private fun PathString.toPlainFileName(): FileNameString {
    // /path/to/post/dir/README.md -> README.md -> README
    return split('/').last().removeMdExtension()
}

@JsExport
fun SlugString.toFilePath(cacheData: String): PathString? {
    val (_, fileNameInfo) = deserialize<CacheData>(cacheData)
    val fileName = toFileName(fileNameInfo.postFolderFullPath, fileNameInfo.duplicatedFile)
    return fileNameInfo.fileNameToPath[fileName]
}

@JsExport
fun getCacheData(): Promise<String> = backendCoroutine.promise {
    serialize(cacheData.get()!!)
}

@JsExport
fun getContent(fileNameString: FileNameString, content: String, cacheData: String, router: NextRouter?): FC<Props> {
    val (dependingLinks, fileNameInfo) = deserialize<CacheData>(cacheData)
    return convertMarkdownToReactElement(fileNameString, content, dependingLinks, fileNameInfo, router)
}

private fun String.removeMdExtension(): String = replace(".md", "")

inline fun <reified T : @Serializable Any> deserialize(jsonString: String): T = json.decodeFromString(jsonString)

inline fun <reified T : @Serializable Any> serialize(obj: T): String = json.encodeToString(obj)

@Serializable
data class CacheData(val dependencyData: DependencyData, val fileNameInfo: FileNameInfo)
