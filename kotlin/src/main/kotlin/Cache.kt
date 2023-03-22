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
        val plainFileName = filePath.toPlainFileName()
        if (nameCache.contains(plainFileName)) {
            duplicatedFile += plainFileName
            val duplicatedPath = fileNameToPath.remove(plainFileName) ?: error("DuplicatedName:$plainFileName is not found in fileNameToPath")
            val duplicatedFileName = duplicatedPath.toFileName()
            fileNameToPath[duplicatedFileName] = duplicatedPath
            val duplicatedSlug = fileNameToSlug.remove(plainFileName)!!
            fileNameToSlug[duplicatedFileName] = duplicatedSlug
        }
        val fileName = filePath.toFileName()

        fileNameToPath[fileName] = filePath
        val slug = toSlug(filePath)
        fileNameToSlug[fileName] = slug

        nameCache += plainFileName
    }

    getAllFiles().forEach { filePath ->
        val content = readContent(filePath)
        // Analyze Dependencies
        convertMarkdownToReactElement(filePath.toFileName(), content)
    }
}

@JsExport
fun PathString.toFileName(): FileNameString {
    val plainFileName = toPlainFileName()
    return if (!duplicatedFile.contains(plainFileName)) {
        plainFileName
    } else {
        // /path/to/post/dir/README.md -> /dir/README.md -> dir/README.md -> dir/README
        return replace(postFolder, "").substring(1).removeMdExtension()
    }
}

private fun PathString.toPlainFileName(): FileNameString {
    // /path/to/post/dir/README.md -> README.md -> README
    return split('/').last().removeMdExtension()
}

@JsExport
fun getContent(fileNameString: FileNameString, content: String): FC<RoutableProps> {
    return convertMarkdownToReactElement(fileNameString, content)
}

@JsExport
val slugs get() = fileNameToSlug.values.toTypedArray()

private fun String.removeMdExtension(): String = replace(".md", "")
