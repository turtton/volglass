package external

import CanvasData
import react.FC
import react.Props

/**
 * contentGetter: (slug -> ContentFC)
 */
typealias CanvasRender = (canvasData: CanvasData, contentGetter: (String) -> FC<Props>) -> FC<Props>
