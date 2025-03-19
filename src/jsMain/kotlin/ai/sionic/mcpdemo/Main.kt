package ai.sionic.mcpdemo

import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    val container = document.getElementById("root") ?: error("root container not found")
    createRoot(container).render(App.create())
}
