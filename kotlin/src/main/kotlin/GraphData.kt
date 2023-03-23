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
data class Node(val id: SlugString, val label: String)

@JsExport
data class Edge(val source: SlugString, val target: SlugString)

@JsExport
fun getRawGraphData(slugString: SlugString, cacheData: String): GraphData {
    val nodes = mutableSetOf<Node>()
    val edges = mutableSetOf<Edge>()
    val (dependencyData, fileNameInfo) = deserialize<CacheData>(cacheData)
    val targetFileName = slugString.toFileName(fileNameInfo.postFolderFullPath, fileNameInfo.duplicatedFile)
    nodes += Node(slugString, targetFileName)
    dependencyData.dependingLinks[targetFileName]?.forEach {
        val targetSlug = fileNameInfo.fileNameToSlug[it]!!
        nodes += Node(targetSlug, it)
        edges += Edge(slugString, targetSlug)
    }
    dependencyData.linkDependencies[targetFileName]?.forEach {
        val targetSlug = fileNameInfo.fileNameToSlug[it]!!
        nodes += Node(targetSlug, it)
        edges += Edge(targetSlug, slugString)
    }
    return GraphData(nodes.toTypedArray(), edges.toTypedArray())
}
