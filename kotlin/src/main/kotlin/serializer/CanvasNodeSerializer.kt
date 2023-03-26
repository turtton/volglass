package serializer

import CanvasNode
import NodeFile
import NodeText
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object CanvasNodeSerializer : KSerializer<CanvasNode> {
    override val descriptor: SerialDescriptor = CanvasNodeSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): CanvasNode {
        val surrogate = CanvasNodeSurrogate.serializer().deserialize(decoder)
        val nodeData = when (surrogate.type) {
            "file" -> NodeFile(surrogate.file ?: "")
            "text" -> NodeText(surrogate.text ?: "")
            else -> error("Unknown node type:${surrogate.type}")
        }
        return surrogate.run {
            CanvasNode(id, x, y, width, height, nodeData)
        }
    }

    override fun serialize(encoder: Encoder, value: CanvasNode) {
        val nodeData = value.nodeData
        val type = when (nodeData) {
            is NodeFile -> "file"
            is NodeText -> "text"
        }
        val file = if (nodeData is NodeFile) nodeData.file else null
        val text = if (nodeData is NodeText) nodeData.text else null
        value.run {
            val surrogate = CanvasNodeSurrogate(id, x, y, width, height, type, file, text)
            CanvasNodeSurrogate.serializer().serialize(encoder, surrogate)
        }
    }

    @Serializable
    data class CanvasNodeSurrogate(val id: String, val x: Int, val y: Int, val width: Int, val height: Int, val type: String, val file: String? = null, val text: String? = null)
}
