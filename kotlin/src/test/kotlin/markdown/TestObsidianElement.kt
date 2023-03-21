package markdown

import kotlin.test.Test
import mysticfall.kotlin.react.test.ReactTestSupport
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.br
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

    @Test
    fun testObsidianLink2() = doTest(
        "[[link | title]]",
    ) {
        p {
            a {
                // TODO FIX link
                href = "link"
                title = "title"
                +"title"
            }
        }
    }

    @Test
    fun testObsidianLineBreak1() = doTest(
        "aaa\nbbb",
    ) {
        p {
            +"aaa"
            +"\n"
            br()
            +"bbb"
        }
    }
}
