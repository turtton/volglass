@file:OptIn(ExperimentalJsExport::class)

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

val cacheScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

val json = Json

val cacheData: KStore<CacheData> = storeOf("backend.cache")

@Serializable
data class CacheData(val dependencyData: DependencyData, val fileNameInfo: FileNameInfo)

@JsExport
fun initCache(
    getAllFiles: () -> Array<String>,
    getMarkdownFolder: () -> String,
    toSlug: (String) -> String,
    readContent: (String) -> String,
): Promise<Array<String>> = cacheScope.promise {
    val fileNameToSlug = mutableMapOf<FileNameString, SlugString>()
    val fileNameToPath = mutableMapOf<FileNameString, PathString>()
    val duplicatedFile = mutableSetOf<String>()
    val nameCache = mutableSetOf<String>()
    val postFolder = getMarkdownFolder()
    getAllFiles().forEach { filePath ->
        val pathString = PathString(filePath)
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

        fileNameToPath[fileName] = pathString
        val slug = toSlug(filePath)
        fileNameToSlug[fileName] = SlugString(slug)

        nameCache += plainFileName.fileName
    }

    val dependencyData = DependencyData()
    val fileNameInfo = FileNameInfo(postFolder, duplicatedFile, fileNameToPath, fileNameToSlug)
    getAllFiles().forEach { filePath ->
        val content = readContent(filePath)
        // Analyze Dependencies
        convertMarkdownToReactElement(PathString(filePath).toFileName(postFolder, duplicatedFile), content, dependencyData, fileNameInfo, null, null)
    }

    cacheData.set(CacheData(dependencyData, fileNameInfo))
    return@promise fileNameToSlug.values.map { it.slug }.toTypedArray()
}

@JsExport
fun getCacheData(): Promise<String> = cacheScope.promise {
    serialize(cacheData.get()!!)
}

@JsExport
fun getContent(fileNameString: String, content: String, cacheData: String, router: NextRouter, codeEncoder: CodeEncoder): FC<Props> {
    val (dependingLinks, fileNameInfo) = deserialize<CacheData>(cacheData)
    return convertMarkdownToReactElement(FileNameString(fileNameString), content, dependingLinks, fileNameInfo, router, codeEncoder)
}

inline fun <reified T : @Serializable Any> deserialize(jsonString: String): T = json.decodeFromString(jsonString)

inline fun <reified T : @Serializable Any> serialize(obj: T): String = json.encodeToString(obj)
