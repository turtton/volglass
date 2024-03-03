@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.Serializable
import serializer.CanvasEdgeSerializer
import serializer.CanvasNodeSerializer

@JsExport
@Serializable
data class CanvasData(val nodes: Array<CanvasNode>, val edges: Array<CanvasEdge>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as CanvasData

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
@Serializable(CanvasNodeSerializer::class)
data class CanvasNode(val id: String, val x: Int, val y: Int, val width: Int, val height: Int, val nodeData: NodeData, val color: String?)

@JsExport
@Serializable(CanvasEdgeSerializer::class)
data class CanvasEdge(val id: String, val fromNode: String, val fromSide: NodeSide, val toNode: String, val toSide: NodeSide, val color: String?, val label: String?)

@JsExport
sealed interface NodeData

@JsExport
data class NodeText(val text: String) : NodeData

@JsExport
data class NodeFile(val file: String) : NodeData

@JsExport
enum class NodeSide {
    TOP, BOTTOM, RIGHT, LEFT
}
