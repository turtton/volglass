package serializer

import CanvasEdge
import NodeSide
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object CanvasEdgeSerializer : KSerializer<CanvasEdge> {

    override val descriptor: SerialDescriptor = CanvasEdgeSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): CanvasEdge {
        val surrogate = CanvasEdgeSurrogate.serializer().deserialize(decoder)
        val fromSide = NodeSide.valueOf(surrogate.fromSide.uppercase())
        val toSide = NodeSide.valueOf(surrogate.toSide.uppercase())
        return surrogate.run {
            CanvasEdge(id, fromNode, fromSide, toNode, toSide)
        }
    }

    override fun serialize(encoder: Encoder, value: CanvasEdge) {
        val surrogate = value.run {
            CanvasEdgeSurrogate(id, fromNode, fromSide.name.lowercase(), toNode, toSide.name.lowercase())
        }
        CanvasEdgeSurrogate.serializer().serialize(encoder, surrogate)
    }

    @Serializable
    data class CanvasEdgeSurrogate(val id: String, val fromNode: String, val fromSide: String, val toNode: String, val toSide: String)
}