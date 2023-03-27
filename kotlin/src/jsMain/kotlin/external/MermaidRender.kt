package external

import react.StateSetter

/**
 * covert contents to mermaid element
 * resultHtmlSetter == [StateSetter]
 */
typealias MermaidRender = (content: String, resultHtmlSetter: (String) -> Unit) -> Unit
