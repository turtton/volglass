package markdown

import kotlin.test.Test
import mysticfall.kotlin.react.test.ReactTestSupport
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.p

class TestObsidianElement : ReactTestSupport {
    @Test
    fun testObsidianLink1() = doTest(
        "[[link]]",
    ) {
        p {
            a {
                // TODO FIX link
                href = "link"
                title = "link"
                +"link"
            }
        }
    }
}
