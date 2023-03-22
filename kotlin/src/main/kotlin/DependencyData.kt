import kotlinx.serialization.Serializable

@Serializable
data class DependencyData(
    /**
     * key -> values
     */
    val dependingLinks: MutableMap<FileNameString, MutableList<FileNameString>> = mutableMapOf(),
    /**
     * key <- values
     */
    val linkDependencies: MutableMap<FileNameString, MutableList<FileNameString>> = mutableMapOf(),
)
