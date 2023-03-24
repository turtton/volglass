package markdown

import kotlin.test.Test
import mysticfall.kotlin.react.test.ReactTestSupport
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.code
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
    fun testObsidianLink3() = doTest(
        "[[link|title]]",
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

    @Test
    fun testObsidianLineBreak2() = doTest(
        "`line1`\n`line2`",
    ) {
        p {
            code {
                +"line1"
            }
            +"\n"
            br()
            code {
                +"line2"
            }
        }
    }
}
