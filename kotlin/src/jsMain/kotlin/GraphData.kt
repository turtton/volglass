@file:OptIn(ExperimentalJsExport::class)

@JsExport
data class GraphData(val nodes: Array<Node>, val edges: Array<Edge>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as GraphData

        if (!nodes.contentEquals(other.nodes)) return false
        if (!edges.contentEquals(other.edges)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nodes.contentHashCode()
        result = 31 * result + edges.contentHashCode()
        return result
    }
}

@JsExport
data class Node(val id: String, val label: String)

@JsExport
data class Edge(val source: String, val target: String)

@JsExport
fun getRawGraphData(slugString: String, cacheData: String): GraphData {
    val nodes = mutableSetOf<Node>()
    val edges = mutableSetOf<Edge>()
    val (dependencyData, fileNameInfo) = deserialize<CacheData>(cacheData)
    val slug = SlugString(slugString)
    val targetFileName = slug.toFileName(fileNameInfo.duplicatedFile)
    nodes += Node(slugString, targetFileName.fileName)
    dependencyData.dependingLinks[targetFileName]?.forEach {
        val targetSlug = fileNameInfo.fileNameToSlug[it] ?: error("Failed to get slug. FileName:$it")
        nodes += Node(targetSlug.slug, it.fileName)
        edges += Edge(slugString, targetSlug.slug)
    }
    dependencyData.linkDependencies[targetFileName]?.forEach {
        val targetSlug = fileNameInfo.fileNameToSlug[it] ?: error("Failed to get slug. FileName:$it")
        nodes += Node(targetSlug.slug, it.fileName)
        edges += Edge(targetSlug.slug, slugString)
    }
    return GraphData(nodes.toTypedArray(), edges.toTypedArray())
}
