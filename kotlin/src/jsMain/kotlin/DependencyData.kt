import kotlinx.serialization.Serializable

@Serializable
data class DependencyData(
    /**
     * key -> values
     */
    val dependingLinks: MutableMap<FileNameString, MutableSet<FileNameString>> = mutableMapOf(),
    /**
     * key <- values
     */
    val linkDependencies: MutableMap<FileNameString, MutableSet<FileNameString>> = mutableMapOf(),
    /**
     * Preloaded contents
     */
    val embedContents: MutableMap<FileNameString, String> = mutableMapOf(),
)
