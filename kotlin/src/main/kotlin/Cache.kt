@file:OptIn(ExperimentalJsExport::class)

import markdown.convertMarkdownToReactElement
import react.FC

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

/**
 * This String should be plain file name
 *
 * Related: [toFileName]
 */
private val duplicatedFile = mutableSetOf<String>()

val fileNameToPath = mutableMapOf<FileNameString, PathString>()
val fileNameToSlug = mutableMapOf<FileNameString, SlugString>()

/**
 * key -> values
 */
val dependingLinks = mutableMapOf<FileNameString, MutableList<FileNameString>>()

/**
 * key <- values
 */
val linkDependencies = mutableMapOf<FileNameString, MutableList<FileNameString>>()

private lateinit var postFolder: String

@JsExport
fun initCache(getAllFiles: () -> Array<String>, getMarkdownFolder: () -> String, toSlug: (String) -> String, readContent: (PathString) -> String) {
    val nameCache = arrayListOf<String>()
    postFolder = getMarkdownFolder()
    getAllFiles().forEach { filePath ->
        val fileName = filePath.toFileName(nameCache.toTypedArray())
        fileNameToPath[fileName] = filePath
        val slug = toSlug(filePath)
        fileNameToSlug[fileName] = slug

        val content = readContent(filePath)
        // Analyze Dependencies
        convertMarkdownToReactElement(fileName, content)

        nameCache += filePath
    }
}

@JsExport
fun PathString.toFileName(nameCache: Array<String>? = null): FileNameString {
    val plainFileName = split('/').last().removeMdExtension()
    return if (nameCache?.contains(plainFileName) != true && !duplicatedFile.contains(plainFileName)) {
        plainFileName
    } else {
        duplicatedFile += plainFileName
        // /path/to/post/README.md -> /README.md -> README.md -> README
        return replace(postFolder, "").substring(1).removeMdExtension()
    }
}

@JsExport
fun getContent(fileNameString: FileNameString, content: String): FC<RoutableProps> {
    return convertMarkdownToReactElement(fileNameString, content)
}

@JsExport
val slugs get() = fileNameToSlug.values.toTypedArray()

private fun String.removeMdExtension(): String = replace(".md", "")
