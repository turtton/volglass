package markdown

import csstype.ClassName
import kotlin.test.Test
import mysticfall.kotlin.react.test.ReactTestSupport
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.strong

class TestObsidianElement : ReactTestSupport {
    @Test
    fun testObsidianLink1() = doTest(
        "[[link]]",
    ) {
        div {
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
        div {
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
        div {
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
        div {
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
        div {
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

    @Test
    fun testObsidianEmbedLink1() = doTest(
        "![[link]]",
    ) {
        div {
            div {
                className = ClassName("border-l-2 px-4 border-gray-300 dark:border-gray-600")
                strong {
                    +"link"
                }
                div()
            }
        }
    }

    @Test
    fun testTeX1() = doTest(
        "$\$a$$",
    ) {
    }

    @Test
    fun testTeX2() = doTest(
        "$\$a\nb$$",
    ) {
    }
}
