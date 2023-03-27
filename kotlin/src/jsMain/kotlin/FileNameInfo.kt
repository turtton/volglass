import kotlinx.serialization.Serializable

@Serializable
data class FileNameInfo(
    /**
     * /path/to/project/post
     */
    val postFolderFullPath: String = "",
    /**
     * This String should be plain file name
     */
    val duplicatedFile: Set<String> = emptySet(),
    val fileNameToPath: Map<FileNameString, PathString> = emptyMap(),
    val fileNameToSlug: Map<FileNameString, SlugString> = emptyMap(),
    /**
     * Static data slugs that is in public folder
     */
    val fileNameToMediaSlug: Map<FileNameString, SlugString> = emptyMap(),
)
