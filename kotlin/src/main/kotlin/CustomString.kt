@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.Serializable

/**
 * Refers actual file
 *
 * Examples: /path/to/project/post/README.md, /path/to/project/post/dir/Content.md
 */
@Serializable
value class PathString(val path: String) {
    init {
        require(path.startsWith('/')) {
            "does not start with `/`. path:$path"
        }
        require(path.contains('.')) {
            "does not contains `.`. path: $path"
        }
    }

    fun toFileName(postFolder: String, duplicatedFile: Set<String>): FileNameString {
        return toSlugString(postFolder).toFileName(duplicatedFile)
    }

    fun toSlugString(postFolder: String): SlugString {
        // /path/to/post/dir/README.md -> /dir/README.md -> /dir/README
        return SlugString(path.replace(postFolder, "").removeMdExtension())
    }
}

/**
 * Refers web slug
 *
 * Examples: /README, /path/Content
 */
@Serializable
value class SlugString(val slug: String) {
    init {
        require(slug.startsWith('/')) {
            "does not start with `/`. slug:$slug"
        }
        require(!slug.contains("\\.md$".toRegex())) {
            "contains `.md`. slug:$slug"
        }
    }

    fun toFilePath(cacheData: CacheData): PathString? {
        val fileNameInfo = cacheData.fileNameInfo
        val fileName = toFileName(fileNameInfo.duplicatedFile)
        return fileNameInfo.fileNameToPath[fileName]
    }

    fun toFileName(duplicatedFile: Set<String>): FileNameString {
        val plainFileName = toPlainFileName()
        return if (!duplicatedFile.contains(plainFileName.fileName)) {
            plainFileName
        } else {
            // /dir/README -> dir/README
            FileNameString(slug.substring(1))
        }
    }

    fun toPlainFileName(): FileNameString {
        // /dir/README -> README
        return FileNameString(slug.split('/').last())
    }
}

/**
 * Includes extension expect markdown(.md)
 *
 * This may contain path if same name file exists.
 *
 * Related: [SlugString.toFileName]
 */
@Serializable
value class FileNameString(val fileName: String) {
    init {
        require(!fileName.startsWith('/')) {
            "starts with `/`. fileName:$fileName"
        }
        require(fileName.contains("\\.[a-zA-Z0-9]*^".toRegex()) || !fileName.contains("\\.md$".toRegex())) {
            "does not contain extension or contains `.md` fileName:$fileName"
        }
    }

    val isMediaFile: Boolean
        get() = isImageFile || isSoundFile

    val isImageFile: Boolean
        get() = fileName.contains("\\.(jpg|jpeg|png|gif|svg|webp)$".toRegex())

    val isSoundFile: Boolean
        get() = fileName.contains("\\.(mp3|ogg)$".toRegex())
}

@JsExport
fun toFilePath(slug: String, cacheData: String): String? {
    return SlugString(slug).toFilePath(deserialize(cacheData))?.path
}

@JsExport
fun toFileName(slug: String, cacheData: String): String {
    val (_, fileNameInfo) = deserialize<CacheData>(cacheData)
    return SlugString(slug).toFileName(fileNameInfo.duplicatedFile).fileName
}

fun String.removeMdExtension(): String = replace("\\.md$".toRegex(), "")
