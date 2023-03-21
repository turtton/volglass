import react.Props

typealias PushAction = (SlugString) -> Unit

@OptIn(ExperimentalJsExport::class)
@JsExport
external interface RoutableProps : Props {
    var push: PushAction
}
