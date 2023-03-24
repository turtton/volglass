import react.create

// fun main() {
//     val container = document.createElement("div")
//     document.body!!.appendChild(container)
//     createRoot(container).render(welcome)
// }

val welcome = Welcome.create {
    name = "Kotlin/JS"
}
