package markdown

import js.core.jso
import kotlin.test.Ignore
import kotlin.test.Test
import mysticfall.kotlin.react.test.ReactTestSupport
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.blockquote
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.em
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.h4
import react.dom.html.ReactHTML.h5
import react.dom.html.ReactHTML.h6
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.img
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.pre
import react.dom.html.ReactHTML.strong
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.tr
import react.dom.html.ReactHTML.ul
import web.cssom.ClassName

/**
 * Related [CommonMarkSpecTest.kt](https://github.com/JetBrains/markdown/blob/master/src/commonTest/kotlin/org/intellij/markdown/CommonMarkSpecTest.kt)
 */
class TestReactElementGenerator : ReactTestSupport {
    @Test
    fun testTabsExample1() = doTest("\tfoo\tbaz\t\tbim\n") {
        pre {
            code {
                +"foo\tbaz\t\tbim"
                +"\n"
            }
        }
    }

    @Test
    fun testTabsExample2() = doTest("  \tfoo\tbaz\t\tbim\n") {
        pre {
            code {
                +"foo\tbaz\t\tbim"
                +"\n"
            }
        }
    }

    @Test
    fun testTabsExample3() = doTest("    a\ta\n    ὐ\ta\n") {
        pre {
            code {
                +"a\ta"
                +"\n"
                +"ὐ\ta"
                +"\n"
            }
        }
    }

    @Test
    fun testTabsExample4() = doTest("  - foo\n\n\tbar\n") {
        ul {
            li {
                div {
                    +"foo"
                }
                div {
                    +"bar"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testTabsExample5() = doTest("- foo\n\n\t\tbar\n") {
        ul {
            li {
                div {
                    +"foo"
                }
                pre {
                    code {
                        +"  bar\n"
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testTabsExample6() = doTest(">\t\tfoo\n") {
        blockquote {
            pre {
                code {
                    +"  foo\n"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testTabsExample7() = doTest("-\t\tfoo\n") {
        ul {
            li {
                pre {
                    code {
                        +"  foo\n"
                    }
                }
            }
        }
    }

    @Test
    fun testTabsExample8() = doTest("    foo\n\tbar\n") {
        pre {
            code {
                +"foo"
                +"\n"
                +"bar"
                +"\n"
            }
        }
    }

    @Test
    fun testTabsExample9() = doTest(" - foo\n   - bar\n\t - baz\n") {
        ul {
            li {
                +"foo"
                ul {
                    li {
                        +"bar"
                        ul {
                            li {
                                +"baz"
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testTabsExample10() = doTest("#\tFoo\n") {
        h1 {
            +"Foo"
        }
    }

    @Test
    fun testTabsExample11() = doTest("*\t*\t*\t\n") {
        hr {}
    }

    /**
     * Modified
     * Reason: To detect TeX code, duller char is treated as special MarkdownToken.
     * Related: [TestObsidianElement.testTeX1]
     *
     * Reason: Disabled substitution of specific escape characters.
     * Related: [ReactElementGenerator.ReactElementGeneratingVisitor.visitLeaf]
     */
    @Test
    fun testBackslashEscapesExample12() =
        doTest("\\!\\\"\\#\\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\=\\>\\?\\@\\[\\\\\\]\\^\\_\\`\\{\\|\\}\\~\n") {
            div {
                +"\\!\\\"\\#\\"
                +"$"
                +"\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\=\\>\\?\\@\\[\\\\\\]\\^\\_"
                +"\\`"
                +"\\{\\|\\}\\~"
            }
        }

    @Test
    fun testBackslashEscapesExample13() = doTest("\\\t\\A\\a\\ \\3\\φ\\«\n") {
        div {
            +"\\"
            +"\t"
            +"\\A\\a\\"
            +" "
            +"\\3\\φ\\«"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     *
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testBackslashEscapesExample14() =
        doTest("\\*not emphasized*\n\\<br/> not a tag\n\\[not a link](/foo)\n\\`not code`\n1\\. not a list\n\\* not a list\n\\# not a heading\n\\[foo]: /url \"not a reference\"\n\\&ouml; not a character entity\n") {
            div {
                +"\\*not emphasized"
                +"*"
                +"\n"
                br()
                +"\\<br/"
                +">"
                +" "
                +"not a tag"
                +"\n"
                br()
                +"\\[not a link"
                +"]"
                +"("
                +"/foo"
                +")"
                +"\n"
                br()
                +"\\`"
                +"not code"
                +"`"
                +"\n"
                br()
                +"1\\."
                +" "
                +"not a list"
                +"\n"
                br()
                +"\\*"
                +" "
                +"not a list"
                +"\n"
                br()
                +"\\#"
                +" "
                +"not a heading"
                +"\n"
                br()
                +"\\[foo"
                +"]"
                +":"
                +" "
                +"/url"
                +" "
                +"\""
                +"not a reference"
                +"\""
                +"\n"
                br()
                +"\\&ouml;"
                +" "
                +"not a character entity"
            }
        }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testBackslashEscapesExample15() = doTest("\\\\*emphasis*\n") {
        div {
            +"\\\\"
            em {
                +"emphasis"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample16() = doTest("foo\\\nbar\n") {
        div {
            +"foo"
            br()
            +"\n"
            +"bar"
        }
    }

    @Test
    fun testBackslashEscapesExample17() = doTest("`` \\[\\` ``\n") {
        div {
            code {
                +"\\[\\`"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample18() = doTest("    \\[\\]\n") {
        pre {
            code {
                +"\\[\\]"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testBackslashEscapesExample19() = doTest("~~~\n\\[\\]\n~~~\n") {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"\\[\\]"
                +"\n"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample20() = doTest("<http://example.com?find=\\*>\n") {
        div {
            a {
                href = "http://example.com?find=%5C*"
                +"http://example.com?find=\\*"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample21() = doTest("<a href=\"/bar\\/)\">\n") {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<a href=\"/bar\\/)\">\n"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample22() = doTest("[foo](/bar\\* \"ti\\*tle\")\n") {
        div {
            a {
                href = "/bar*"
                title = "ti*tle"
                +"foo"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample23() = doTest("[foo]\n\n[foo]: /bar\\* \"ti\\*tle\"\n") {
        div {
            a {
                href = "/bar*"
                title = "ti*tle"
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     *
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testBackslashEscapesExample24() = doTest("``` foo\\+bar\nfoo\n```\n") {
        pre {
            className = ClassName("language-foo\\+bar")
            code {
                className = ClassName("language-foo\\+bar")
                +"foo"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample25() =
        doTest("&nbsp; &amp; &copy; &AElig; &Dcaron;\n&frac34; &HilbertSpace; &DifferentialD;\n&ClockwiseContourIntegral; &ngE;\n") {
            div {
                +"  &amp; © Æ Ď\n¾ ℋ ⅆ\n∲ ≧̸"
            }
        }

    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample26() = doTest("&#35; &#1234; &#992; &#0;\n") {
        div {
            +"# Ӓ Ϡ �"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample27() = doTest("&#X22; &#XD06; &#xcab;\n") {
        div {
            +"&#X22;"
            +" "
            +"&#XD06;"
            +" "
            +"&#xcab;"
        }
    }

    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample28() = doTest(
        "&nbsp &x; &#; &#x;\n&#87654321;\n&#abcdef0;\n&ThisIsNotDefined; &hi?;\n",
    ) {
        div {
            +"&nbsp &x; &#; &#x;\n&#87654321;\n&#abcdef0;\n&ThisIsNotDefined; &hi?;"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample29() = doTest(
        markdown = "&copy\n",
    ) {
        div {
            +"&copy"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample30() = doTest(markdown = "&MadeUpEntity;\n") {
        div {
            +"&MadeUpEntity;"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample31() = doTest(
        markdown = "<a href=\"&ouml;&ouml;.html\">\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<a href=\"&ouml;&ouml;.html\">\n"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample32() = doTest(
        markdown = "[foo](/f&ouml;&ouml; \"f&ouml;&ouml;\")\n",
    ) {
        div {
            a {
                href = "/f%C3%B6%C3%B6"
                title = "föö"
                +"foo"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample33() = doTest(
        markdown = "[foo]\n\n[foo]: /f&ouml;&ouml; \"f&ouml;&ouml;\"\n",
    ) {
        div {
            a {
                href = "/f%C3%B6%C3%B6"
                title = "föö"
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample34() = doTest(
        markdown = "``` f&ouml;&ouml;\nfoo\n```\n",
    ) {
        pre {
            className = ClassName("language-f&ouml;&ouml;")
            code {
                className = ClassName("language-f&ouml;&ouml;")
                +"foo"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample35() = doTest(
        markdown = "`f&ouml;&ouml;`\n",
    ) {
        div {
            code {
                +"f&ouml;&ouml;"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample36() = doTest(
        markdown = "    f&ouml;f&ouml;\n",
    ) {
        pre {
            code {
                +"f&ouml;f&ouml;"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     *
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample37() = doTest(
        markdown = "&#42;foo&#42;\n*foo*\n",
    ) {
        div {
            +"&#42;foo&#42;"
            +"\n"
            br()
            em {
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample38() = doTest(
        markdown = "&#42; foo\n\n* foo\n",
    ) {
        div {
            +"&#42;"
            +" "
            +"foo"
        }
        ul {
            li {
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample39() = doTest(
        markdown = "foo&#10;&#10;bar\n",
    ) {
        div {
            +"foo&#10;&#10;bar"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEntityAndNumericCharacterReferencesExample40() = doTest(
        markdown = "&#9;foo\n",
    ) {
        div {
            +"&#9;foo"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample41() = doTest(
        markdown = "[a](url &quot;tit&quot;)\n",
    ) {
        div {
            +"["
            +"a"
            +"]"
            +"("
            +"url"
            +" "
            +"&quot;tit&quot;"
            +")"
        }
    }

    @Test
    fun testPrecedenceExample42() = doTest(
        markdown = "- `one\n- two`\n",
    ) {
        ul {
            li {
                +"`"
                +"one"
            }
            li {
                +"two"
                +"`"
            }
        }
    }

    @Test
    fun testThematicBreaksExample43() = doTest(
        markdown = "***\n---\n___\n",
    ) {
        hr()
        hr()
        hr()
    }

    @Test
    fun testThematicBreaksExample44() = doTest(
        markdown = "+++\n",
    ) {
        div {
            +"+++"
        }
    }

    @Test
    fun testThematicBreaksExample45() = doTest(
        markdown = "===\n",
    ) {
        div {
            +"==="
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testThematicBreaksExample46() = doTest(
        markdown = "--\n**\n__\n",
    ) {
        div {
            +"--"
            +"\n"
            br()
            +"*"
            +"*"
            +"\n"
            br()
            +"_"
            +"_"
        }
    }

    @Test
    fun testThematicBreaksExample47() = doTest(
        markdown = " ***\n  ***\n   ***\n",
    ) {
        hr()
        hr()
        hr()
    }

    @Test
    fun testThematicBreaksExample48() = doTest(
        markdown = "    ***\n",
    ) {
        pre {
            code {
                +"***"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testThematicBreaksExample49() = doTest(
        markdown = "Foo\n    ***\n",
    ) {
        div {
            +"Foo"
            +"\n"
            +"***"
        }
    }

    @Test
    fun testThematicBreaksExample50() = doTest(
        markdown = "_____________________________________\n",
    ) {
        hr()
    }

    @Test
    fun testThematicBreaksExample51() = doTest(
        markdown = " - - -\n",
    ) {
        hr()
    }

    @Test
    fun testThematicBreaksExample52() = doTest(
        markdown = " **  * ** * ** * **\n",
    ) {
        hr()
    }

    @Test
    fun testThematicBreaksExample53() = doTest(
        markdown = "-     -      -      -\n",
    ) {
        hr()
    }

    @Test
    fun testThematicBreaksExample54() = doTest(
        markdown = "- - - -    \n",
    ) {
        hr()
    }

    @Test
    fun testThematicBreaksExample55() = doTest(
        markdown = "_ _ _ _ a\n\na------\n\n---a---\n",
    ) {
        div {
            +"_"
            +" _ "
            +"_"
            +" _ a"
        }
        div {
            +"a------"
        }
        div {
            +"---a---"
        }
    }

    @Test
    fun testThematicBreaksExample56() = doTest(
        markdown = " *-*\n",
    ) {
        div {
            em {
                +"-"
            }
        }
    }

    @Test
    fun testThematicBreaksExample57() = doTest(
        markdown = "- foo\n***\n- bar\n",
    ) {
        ul {
            li {
                +"foo"
            }
        }
        hr()
        ul {
            li {
                +"bar"
            }
        }
    }

    @Test
    fun testThematicBreaksExample58() = doTest(
        markdown = "Foo\n***\nbar\n",
    ) {
        div {
            +"Foo"
        }
        hr()
        div {
            +"bar"
        }
    }

    @Test
    fun testThematicBreaksExample59() = doTest(
        markdown = "Foo\n---\nbar\n",
    ) {
        h2 {
            +"Foo"
        }
        div {
            +"bar"
        }
    }

    @Test
    fun testThematicBreaksExample60() = doTest(
        markdown = "* Foo\n* * *\n* Bar\n",
    ) {
        ul {
            li {
                +"Foo"
            }
        }
        hr()
        ul {
            li {
                +"Bar"
            }
        }
    }

    @Test
    fun testThematicBreaksExample61() = doTest(
        markdown = "- Foo\n- * * *\n",
    ) {
        ul {
            li {
                +"Foo"
            }
            li {
                hr()
            }
        }
    }

    @Test
    fun testATXHeadingsExample62() = doTest(
        markdown = "# foo\n## foo\n### foo\n#### foo\n##### foo\n###### foo\n",
    ) {
        h1 {
            +"foo"
        }
        h2 {
            +"foo"
        }
        h3 {
            +"foo"
        }
        h4 {
            +"foo"
        }
        h5 {
            +"foo"
        }
        h6 {
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample63() = doTest(
        markdown = "####### foo\n",
    ) {
        div {
            +"#######"
            +" "
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample64() = doTest(
        markdown = "#5 bolt\n\n#hashtag\n",
    ) {
        div {
            +"#5 bolt"
        }
        div {
            +"#hashtag"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testATXHeadingsExample65() = doTest(
        markdown = "\\## foo\n",
    ) {
        div {
            +"\\##"
            +" "
            +"foo"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testATXHeadingsExample66() = doTest(
        markdown = "# foo *bar* \\*baz\\*\n",
    ) {
        h1 {
            +"foo"
            +" "
            em {
                +"bar"
            }
            +" "
            +"\\*baz\\*"
        }
    }

    @Test
    fun testATXHeadingsExample67() = doTest(
        markdown = "#                  foo                     \n",
    ) {
        h1 {
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample68() = doTest(
        markdown = " ### foo\n  ## foo\n   # foo\n",
    ) {
        h3 {
            +"foo"
        }
        h2 {
            +"foo"
        }
        h1 {
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample69() = doTest(
        markdown = "    # foo\n",
    ) {
        pre {
            code {
                +"# foo"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testATXHeadingsExample70() = doTest(
        markdown = "foo\n    # bar\n",
    ) {
        div {
            +"foo\n# bar"
        }
    }

    @Test
    fun testATXHeadingsExample71() = doTest(
        markdown = "## foo ##\n  ###   bar    ###\n",
    ) {
        h2 {
            +"foo"
        }
        h3 {
            +"bar"
        }
    }

    @Test
    fun testATXHeadingsExample72() = doTest(
        markdown = "# foo ##################################\n##### foo ##\n",
    ) {
        h1 {
            +"foo"
        }
        h5 {
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample73() = doTest(
        markdown = "### foo ###     \n",
    ) {
        h3 {
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample74() = doTest(
        markdown = "### foo ### b\n",
    ) {
        h3 {
            +"foo"
            +" "
            +"###"
            +" "
            +"b"
        }
    }

    @Test
    fun testATXHeadingsExample75() = doTest(
        markdown = "# foo#\n",
    ) {
        h1 {
            +"foo#"
        }
    }

    @Test
    fun testATXHeadingsExample76() = doTest(
        markdown = "### foo \\###\n## foo #\\##\n# foo \\#\n",
    ) {
        h3 {
            +"foo"
            +" "
            +"\\###"
        }
        h2 {
            +"foo"
            +" "
            +"#\\##"
        }
        h1 {
            +"foo"
            +" "
            +"\\#"
        }
    }

    @Test
    fun testATXHeadingsExample77() = doTest(
        markdown = "****\n## foo\n****\n",
    ) {
        hr()
        h2 {
            +"foo"
        }
        hr()
    }

    @Test
    fun testATXHeadingsExample78() = doTest(
        markdown = "Foo bar\n# baz\nBar foo\n",
    ) {
        div {
            +"Foo bar"
        }
        h1 {
            +"baz"
        }
        div {
            +"Bar foo"
        }
    }

    @Test
    fun testATXHeadingsExample79() = doTest(
        markdown = "## \n#\n### ###\n",
    ) {
        h2()
        h1()
        h3()
    }

    @Test
    fun testSetextHeadingsExample80() = doTest(
        markdown = "Foo *bar*\n=========\n\nFoo *bar*\n---------\n",
    ) {
        h1 {
            +"Foo"
            +" "
            em {
                +"bar"
            }
        }
        h2 {
            +"Foo"
            +" "
            em {
                +"bar"
            }
        }
    }

    @Test
    @Ignore
    fun testSetextHeadingsExample81() = doTest(
        markdown = "Foo *bar\nbaz*\n====\n",
    ) {
        h1 {
            +"Foo "
            em {
                +"bar\nbaz"
            }
        }
    }

    @Test
    @Ignore
    fun testSetextHeadingsExample82() = doTest(
        markdown = "  Foo *bar\nbaz*\t\n====\n",
    ) {
        h1 {
            +"Foo "
            em {
                +"bar\nbaz"
            }
        }
    }

    @Test
    fun testSetextHeadingsExample83() = doTest(
        markdown = "Foo\n-------------------------\n\nFoo\n=\n",
    ) {
        h2 {
            +"Foo"
        }
        h1 {
            +"Foo"
        }
    }

    @Test
    fun testSetextHeadingsExample84() = doTest(
        markdown = "   Foo\n---\n\n  Foo\n-----\n\n  Foo\n  ===\n",
    ) {
        h2 {
            +"Foo"
        }
        h2 {
            +"Foo"
        }
        h1 {
            +"Foo"
        }
    }

    @Test
    fun testSetextHeadingsExample85() = doTest(
        markdown = "    Foo\n    ---\n\n    Foo\n---\n",
    ) {
        pre {
            code {
                +"Foo"
                +"\n"
                +"---"
                +"\n"
                +"\n"
                +"Foo"
                +"\n"
            }
        }
        hr()
    }

    @Test
    fun testSetextHeadingsExample86() = doTest(
        markdown = "Foo\n   ----      \n",
    ) {
        h2 {
            +"Foo"
        }
    }

    @Test
    @Ignore
    fun testSetextHeadingsExample87() = doTest(
        markdown = "Foo\n    ---\n",
    ) {
        div {
            +"Foo\n---"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testSetextHeadingsExample88() = doTest(
        markdown = "Foo\n= =\n\nFoo\n--- -\n",
    ) {
        div {
            +"Foo"
            +"\n"
            br()
            +"="
            +" "
            +"="
        }
        div {
            +"Foo"
        }
        hr()
    }

    @Test
    fun testSetextHeadingsExample89() = doTest(
        markdown = "Foo  \n-----\n",
    ) {
        h2 {
            +"Foo"
        }
    }

    @Test
    fun testSetextHeadingsExample90() = doTest(
        markdown = "Foo\\\n----\n",
    ) {
        h2 {
            +"Foo\\"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testSetextHeadingsExample91() = doTest(
        markdown = "`Foo\n----\n`\n\n<a title=\"a lot\n---\nof dashes\"/>\n",
    ) {
        h2 {
            +"`"
            +"Foo"
        }
        div {
            +"`"
        }
        h2 {
            +"<"
            +"a title="
            +"\""
            +"a lot"
        }
        div {
            +"of dashes"
            +"\""
            +"/"
            +">"
        }
    }

    @Test
    fun testSetextHeadingsExample92() = doTest(
        markdown = "> Foo\n---\n",
    ) {
        blockquote {
            div {
                +"Foo"
            }
        }
        hr()
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testSetextHeadingsExample93() = doTest(
        markdown = "> foo\nbar\n===\n",
    ) {
        blockquote {
            div {
                +"foo"
                +"\n"
                br()
                +"bar"
                +"\n"
                br()
                +"==="
            }
        }
    }

    @Test
    fun testSetextHeadingsExample94() = doTest(
        markdown = "- Foo\n---\n",
    ) {
        ul {
            li {
                +"Foo"
            }
        }
        hr()
    }

    @Test
    @Ignore
    fun testSetextHeadingsExample95() = doTest(
        markdown = "Foo\nBar\n---\n",
    ) {
        h2 {
            +"Foo\nBar"
        }
    }

    @Test
    fun testSetextHeadingsExample96() = doTest(
        markdown = "---\nFoo\n---\nBar\n---\nBaz\n",
    ) {
        hr()
        h2 {
            +"Foo"
        }
        h2 {
            +"Bar"
        }
        div {
            +"Baz"
        }
    }

    @Test
    fun testSetextHeadingsExample97() = doTest(
        markdown = "\n====\n",
    ) {
        div {
            +"===="
        }
    }

    @Test
    fun testSetextHeadingsExample98() = doTest(
        markdown = "---\n---\n",
    ) {
        hr()
        hr()
    }

    @Test
    fun testSetextHeadingsExample99() = doTest(
        markdown = "- foo\n-----\n",
    ) {
        ul {
            li {
                +"foo"
            }
        }
        hr()
    }

    @Test
    fun testSetextHeadingsExample100() = doTest(
        markdown = "    foo\n---\n",
    ) {
        pre {
            code {
                +"foo"
                +"\n"
            }
        }
        hr()
    }

    @Test
    fun testSetextHeadingsExample101() = doTest(
        markdown = "> foo\n-----\n",
    ) {
        blockquote {
            div {
                +"foo"
            }
        }
        hr()
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testSetextHeadingsExample102() = doTest(
        markdown = "\\> foo\n------\n",
    ) {
        h2 {
            +"\\>"
            +" "
            +"foo"
        }
    }

    @Test
    fun testSetextHeadingsExample103() = doTest(
        markdown = "Foo\n\nbar\n---\nbaz\n",
    ) {
        div {
            +"Foo"
        }
        h2 {
            +"bar"
        }
        div {
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testSetextHeadingsExample104() = doTest(
        markdown = "Foo\nbar\n\n---\n\nbaz\n",
    ) {
        div {
            +"Foo"
            +"\n"
            br()
            +"bar"
        }
        hr()
        div {
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testSetextHeadingsExample105() = doTest(
        markdown = "Foo\nbar\n* * *\nbaz\n",
    ) {
        div {
            +"Foo"
            +"\n"
            br()
            +"bar"
        }
        hr()
        div {
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     *
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testSetextHeadingsExample106() = doTest(
        markdown = "Foo\nbar\n\\---\nbaz\n",
    ) {
        div {
            +"Foo"
            +"\n"
            br()
            +"bar"
            +"\n"
            br()
            +"\\---"
            +"\n"
            br()
            +"baz"
        }
    }

    @Test
    fun testIndentedCodeBlocksExample107() = doTest(
        markdown = "    a simple\n      indented code block\n",
    ) {
        pre {
            code {
                +"a simple"
                +"\n"
                +"  indented code block"
                +"\n"
            }
        }
    }

    @Test
    fun testIndentedCodeBlocksExample108() = doTest(
        markdown = "  - foo\n\n    bar\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
                div {
                    +"bar"
                }
            }
        }
    }

    @Test
    fun testIndentedCodeBlocksExample109() = doTest(
        markdown = "1.  foo\n\n    - bar\n",
    ) {
        ol {
            li {
                div {
                    +"foo"
                }
                ul {
                    li {
                        +"bar"
                    }
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testIndentedCodeBlocksExample110() = doTest(
        markdown = "    <a/>\n    *hi*\n\n    - one\n",
    ) {
        pre {
            code {
                +"<a/>"
                +"\n"
                +"*hi*"
                +"\n"
                +"\n"
                +"- one"
                +"\n"
            }
        }
    }

    @Test
    fun testIndentedCodeBlocksExample111() = doTest(
        markdown = "    chunk1\n\n    chunk2\n  \n \n \n    chunk3\n",
    ) {
        pre {
            code {
                +"chunk1"
                +"\n"
                +"\n"
                +"chunk2"
                +"\n"
                +"\n"
                +"\n"
                +"\n"
                +"chunk3"
                +"\n"
            }
        }
    }

    @Test
    fun testIndentedCodeBlocksExample112() = doTest(
        markdown = "    chunk1\n      \n      chunk2\n",
    ) {
        pre {
            code {
                +"chunk1"
                +"\n"
                +"  "
                +"\n"
                +"  chunk2"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testIndentedCodeBlocksExample113() = doTest(
        markdown = "Foo\n    bar\n\n",
    ) {
        div {
            +"Foo\nbar"
        }
    }

    @Test
    fun testIndentedCodeBlocksExample114() = doTest(
        markdown = "    foo\nbar\n",
    ) {
        pre {
            code {
                +"foo"
                +"\n"
            }
        }
        div {
            +"bar"
        }
    }

    @Test
    fun testIndentedCodeBlocksExample115() = doTest(
        markdown = "# Heading\n    foo\nHeading\n------\n    foo\n----\n",
    ) {
        h1 {
            +"Heading"
        }
        pre {
            code {
                +"foo"
                +"\n"
            }
        }
        h2 {
            +"Heading"
        }
        pre {
            code {
                +"foo"
                +"\n"
            }
        }
        hr()
    }

    @Test
    fun testIndentedCodeBlocksExample116() = doTest(
        markdown = "        foo\n    bar\n",
    ) {
        pre {
            code {
                +"    foo"
                +"\n"
                +"bar"
                +"\n"
            }
        }
    }

    @Test
    fun testIndentedCodeBlocksExample117() = doTest(
        markdown = "\n    \n    foo\n    \n\n",
    ) {
        pre {
            code {
                +"foo"
                +"\n"
            }
        }
    }

    @Test
    fun testIndentedCodeBlocksExample118() = doTest(
        markdown = "    foo  \n",
    ) {
        pre {
            code {
                +"foo  "
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     *
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample119() = doTest(
        markdown = "```\n<\n >\n```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"<"
                +"\n"
                +" >"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     *
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample120() = doTest(
        markdown = "~~~\n<\n >\n~~~\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"<"
                +"\n"
                +" >"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample121() = doTest(
        markdown = "``\nfoo\n``\n",
    ) {
        div {
            code {
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample122() = doTest(
        markdown = "```\naaa\n~~~\n```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"~~~"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample123() = doTest(
        markdown = "~~~\naaa\n```\n~~~\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"```"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample124() = doTest(
        markdown = "````\naaa\n```\n``````\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"```"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample125() = doTest(
        markdown = "~~~~\naaa\n~~~\n~~~~\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"~~~"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample126() = doTest(
        markdown = "```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample127() = doTest(
        markdown = "`````\n\n```\naaa\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"\n"
                +"```"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample128() = doTest(
        markdown = "> ```\n> aaa\n\nbbb\n",
    ) {
        blockquote {
            pre {
                className = ClassName("language-")
                code {
                    className = ClassName("language-")
                    +"aaa"
                    +"\n"
                }
            }
        }
        div {
            +"bbb"
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample129() = doTest(
        markdown = "```\n\n  \n```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"\n"
                +"  "
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample130() = doTest(
        markdown = "```\n```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample131() = doTest(
        markdown = " ```\n aaa\naaa\n```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample132() = doTest(
        markdown = "  ```\naaa\n  aaa\naaa\n  ```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"aaa"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample133() = doTest(
        markdown = "   ```\n   aaa\n    aaa\n  aaa\n   ```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +" aaa"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample134() = doTest(
        markdown = "    ```\n    aaa\n    ```\n",
    ) {
        pre {
            code {
                +"```"
                +"\n"
                +"aaa"
                +"\n"
                +"```"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample135() = doTest(
        markdown = "```\naaa\n  ```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample136() = doTest(
        markdown = "   ```\naaa\n  ```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample137() = doTest(
        markdown = "```\naaa\n    ```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"    ```"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testFencedCodeBlocksExample138() = doTest(
        markdown = "``` ```\naaa\n",
    ) {
        div {
            code()
            +"\naaa"
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample139() = doTest(
        markdown = "~~~~~~\naaa\n~~~ ~~\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"aaa"
                +"\n"
                +"~~~ ~~"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample140() = doTest(
        markdown = "foo\n```\nbar\n```\nbaz\n",
    ) {
        div {
            +"foo"
        }
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"bar"
                +"\n"
            }
        }
        div {
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample141() = doTest(
        markdown = "foo\n---\n~~~\nbar\n~~~\n# baz\n",
    ) {
        h2 {
            +"foo"
        }
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"bar"
                +"\n"
            }
        }
        h1 {
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample142() = doTest(
        markdown = "```ruby\ndef foo(x)\n  return 3\nend\n```\n",
    ) {
        pre {
            className = ClassName("language-ruby")
            code {
                className = ClassName("language-ruby")
                +"def foo(x)"
                +"\n"
                +"  return 3"
                +"\n"
                +"end"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample143() = doTest(
        markdown = "~~~~    ruby startline=3 \$%@#\$\ndef foo(x)\n  return 3\nend\n~~~~~~~\n",
    ) {
        pre {
            className = ClassName("language-ruby")
            code {
                className = ClassName("language-ruby")
                +"def foo(x)"
                +"\n"
                +"  return 3"
                +"\n"
                +"end"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample144() = doTest(
        markdown = "````;\n````\n",
    ) {
        pre {
            className = ClassName("language-;")
            code {
                className = ClassName("language-;")
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample145() = doTest(
        markdown = "``` aa ```\nfoo\n",
    ) {
        div {
            code {
                +"aa"
            }
            +"\n"
            +"foo"
        }
    }

    @Test
    @Ignore
    fun testFencedCodeBlocksExample146() = doTest(
        markdown = "~~~ aa ``` ~~~\nfoo\n~~~\n",
    ) {
        pre {
            code {
                className = ClassName("language-aa")
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testFencedCodeBlocksExample147() = doTest(
        markdown = "```\n``` aaa\n```\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"``` aaa"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testHTMLBlocksExample148() = doTest(
        markdown = "<table><tr><td>\n<pre>\n**Hello**,\n\n_world_.\n</pre>\n</td></tr></table>\n",
    ) {
        table {
            tr {
                td {
                    pre {
                        +"**Hello**,"
                    }
                    div {
                        em {
                            +"world"
                        }
                        +"."
                    }
                }
            }
        }
    }

    @Test
    fun testHTMLBlocksExample149() = doTest(
        markdown = "<table>\n  <tr>\n    <td>\n           hi\n    </td>\n  </tr>\n</table>\n\nokay.\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<table>\n  <tr>\n    <td>\n           hi\n    </td>\n  </tr>\n</table>"
            }
        }
        div {
            +"okay."
        }
    }

    @Test
    fun testHTMLBlocksExample150() = doTest(
        markdown = " <div>\n  *hello*\n         <foo><a>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = " <div>\n  *hello*\n         <foo><a>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample151() = doTest(
        markdown = "</div>\n*foo*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "</div>\n*foo*\n"
            }
        }
    }

    /**
     * FIXME Cannot support this case.
     *
     * expected:
     * ```html
     *  <div class="foo">
     *      <p>
     *          <em>Markdown<em>
     *      </p>
     *  </div>
     *  ```
     *  actual:
     *  ```html
     *  <div class="foo">(</div> <-automatically complemented)
     *  <p>
     *      <em>Markdown<em>
     *  </p>
     *  (<div> <-automatically complemented)</div>
     *  ```
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample152() = doTest(
        markdown = "<DIV CLASS=\"foo\">\n\n*Markdown*\n\n</DIV>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<DIV CLASS=\"foo\">\n<p><em>Markdown</em></p>\n</DIV>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample153() = doTest(
        markdown = "<div id=\"foo\"\n  class=\"bar\">\n</div>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div id=\"foo\"\n  class=\"bar\">\n</div>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample154() = doTest(
        markdown = "<div id=\"foo\" class=\"bar\n  baz\">\n</div>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div id=\"foo\" class=\"bar\n  baz\">\n</div>\n"
            }
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample155() = doTest(
        markdown = "<div>\n*foo*\n\n*bar*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div>\n*foo*\n<p><em>bar</em></p>\n"
            }
        }
    }

    @Test
    @Ignore
    fun testHTMLBlocksExample156() = doTest(
        markdown = "<div id=\"foo\"\n*hi*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div id=\"foo\"\n*hi*\n"
            }
        }
    }

    @Test
    @Ignore
    fun testHTMLBlocksExample157() = doTest(
        markdown = "<div class\nfoo\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div class\nfoo\n"
            }
        }
    }

    @Test
    @Ignore
    fun testHTMLBlocksExample158() = doTest(
        markdown = "<div *???-&&&-<---\n*foo*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div *???-&&&-<---\n*foo*\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample159() = doTest(
        markdown = "<div><a href=\"bar\">*foo*</a></div>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div><a href=\"bar\">*foo*</a></div>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample160() = doTest(
        markdown = "<table><tr><td>\nfoo\n</td></tr></table>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<table><tr><td>\nfoo\n</td></tr></table>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample161() = doTest(
        markdown = "<div></div>\n``` c\nint x = 33;\n```\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div></div>\n``` c\nint x = 33;\n```\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample162() = doTest(
        markdown = "<a href=\"foo\">\n*bar*\n</a>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<a href=\"foo\">\n*bar*\n</a>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample163() = doTest(
        markdown = "<Warning>\n*bar*\n</Warning>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<Warning>\n*bar*\n</Warning>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample164() = doTest(
        markdown = "<i class=\"foo\">\n*bar*\n</i>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<i class=\"foo\">\n*bar*\n</i>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample165() = doTest(
        markdown = "</ins>\n*bar*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "</ins>\n*bar*\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample166() = doTest(
        markdown = "<del>\n*foo*\n</del>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<del>\n*foo*\n</del>\n"
            }
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample167() = doTest(
        markdown = "<del>\n\n*foo*\n\n</del>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<del>\n<p><em>foo</em></p>\n</del>\n"
            }
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample168() = doTest(
        markdown = "<del>*foo*</del>\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<p><del><em>foo</em></del></p>\n"
                }
            }
        }
    }

    @Test
    fun testHTMLBlocksExample169() = doTest(
        markdown = "<pre language=\"haskell\"><code>\nimport Text.HTML.TagSoup\n\nmain :: IO ()\nmain = print \$ parseTags tags\n</code></pre>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html =
                    "<pre language=\"haskell\"><code>\nimport Text.HTML.TagSoup\n\nmain :: IO ()\nmain = print \$ parseTags tags\n</code></pre>"
            }
        }
        div {
            +"okay"
        }
    }

    @Test
    fun testHTMLBlocksExample170() = doTest(
        markdown = "<script type=\"text/javascript\">\n// JavaScript example\n\ndocument.getElementById(\"demo\").innerHTML = \"Hello JavaScript!\";\n</script>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html =
                    "<script type=\"text/javascript\">\n// JavaScript example\n\ndocument.getElementById(\"demo\").innerHTML = \"Hello JavaScript!\";\n</script>"
            }
        }
        div {
            +"okay"
        }
    }

    @Test
    @Ignore
    fun testHTMLBlocksExample171() = doTest(
        markdown = "<textarea>\n\n*foo*\n\n_bar_\n\n</textarea>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<textarea>\n\n*foo*\n\n_bar_\n\n</textarea>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample172() = doTest(
        markdown = "<style\n  type=\"text/css\">\nh1 {color:red;}\n\ndiv {color:blue;}\n</style>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<style\n  type=\"text/css\">\nh1 {color:red;}\n\ndiv {color:blue;}\n</style>"
            }
        }
        div {
            +"okay"
        }
    }

    @Test
    fun testHTMLBlocksExample173() = doTest(
        markdown = "<style\n  type=\"text/css\">\n\nfoo\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<style\n  type=\"text/css\">\n\nfoo\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample174() = doTest(
        markdown = "> <div>\n> foo\n\nbar\n",
    ) {
        blockquote {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<div>\nfoo"
                }
            }
        }
        div {
            +"bar"
        }
    }

    @Test
    fun testHTMLBlocksExample175() = doTest(
        markdown = "- <div>\n- foo\n",
    ) {
        ul {
            li {
                div {
                    dangerouslySetInnerHTML = jso {
                        __html = "<div>"
                    }
                }
            }
            li {
                +"foo"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample176() = doTest(
        markdown = "<style>p{color:red;}</style>\n*foo*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<style>p{color:red;}</style>"
            }
        }
        div {
            em {
                +"foo"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample177() = doTest(
        markdown = "<!-- foo -->*bar*\n*baz*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<!-- foo -->*bar*"
            }
        }
        div {
            em {
                +"baz"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample178() = doTest(
        markdown = "<script>\nfoo\n</script>1. *bar*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<script>\nfoo\n</script>1. *bar*"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample179() = doTest(
        markdown = "<!-- Foo\n\nbar\n   baz -->\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<!-- Foo\n\nbar\n   baz -->"
            }
        }
        div {
            +"okay"
        }
    }

    @Test
    fun testHTMLBlocksExample180() = doTest(
        markdown = "<?php\n\n  echo '>';\n\n?>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<?php\n\n  echo '>';\n\n?>"
            }
        }
        div {
            +"okay"
        }
    }

    @Test
    fun testHTMLBlocksExample181() = doTest(
        markdown = "<!DOCTYPE html>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<!DOCTYPE html>"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample182() = doTest(
        markdown = "<![CDATA[\nfunction matchwo(a,b)\n{\n  if (a < b && a < 0) then {\n    return 1;\n\n  } else {\n\n    return 0;\n  }\n}\n]]>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html =
                    "<![CDATA[\nfunction matchwo(a,b)\n{\n  if (a < b && a < 0) then {\n    return 1;\n\n  } else {\n\n    return 0;\n  }\n}\n]]>"
            }
        }
        div {
            +"okay"
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     *
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample183() = doTest(
        markdown = "  <!-- foo -->\n\n    <!-- foo -->\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "  <!-- foo -->\n<pre><code><!-- foo -->\n</code></pre>\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testHTMLBlocksExample184() = doTest(
        markdown = "  <div>\n\n    <div>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "  <div>"
            }
        }
        pre {
            code {
                +"<div>"
                +"\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample185() = doTest(
        markdown = "Foo\n<div>\nbar\n</div>\n",
    ) {
        div {
            +"Foo"
        }
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div>\nbar\n</div>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample186() = doTest(
        markdown = "<div>\nbar\n</div>\n*foo*\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div>\nbar\n</div>\n*foo*\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testHTMLBlocksExample187() = doTest(
        markdown = "Foo\n<a href=\"bar\">\nbaz\n",
    ) {
        div {
            +"Foo"
            +"\n"
            br()
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"bar\">"
                }
            }
            +"\n"
            +"baz"
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample188() = doTest(
        markdown = "<div>\n\n*Emphasized* text.\n\n</div>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div>\n<p><em>Emphasized</em> text.</p>\n</div>\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample189() = doTest(
        markdown = "<div>\n*Emphasized* text.\n</div>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<div>\n*Emphasized* text.\n</div>\n"
            }
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample190() = doTest(
        markdown = "<table>\n\n<tr>\n\n<td>\nHi\n</td>\n\n</tr>\n\n</table>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<table>\n<tr>\n<td>\nHi\n</td>\n</tr>\n</table>\n"
            }
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     *
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample191() = doTest(
        markdown = "<table>\n\n  <tr>\n\n    <td>\n      Hi\n    </td>\n\n  </tr>\n\n</table>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<table>\n  <tr>\n<pre><code><td>\n  Hi\n</td>\n</code></pre>\n  </tr>\n</table>\n"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample192() = doTest(
        markdown = "[foo]: /url \"title\"\n\n[foo]\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample193() = doTest(
        markdown = "   [foo]: \n      /url  \n           'the title'  \n\n[foo]\n",
    ) {
        div {
            a {
                href = "/url"
                title = "the title"
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinkReferenceDefinitionsExample194() = doTest(
        markdown = "[Foo*bar\\]]:my_(url) 'title (with parens)'\n\n[Foo*bar\\]]\n",
    ) {
        div {
            a {
                href = "my_(url)"
                title = "title (with parens)"
                +"Foo"
                +"*"
                +"bar\\]"
            }
        }
    }

    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample195() = doTest(
        markdown = "[Foo bar]:\n<my url>\n'title'\n\n[Foo bar]\n",
    ) {
        div {
            a {
                href = "my%20url"
                title = "title"
                +"Foo bar"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample196() = doTest(
        markdown = "[foo]: /url '\ntitle\nline1\nline2\n'\n\n[foo]\n",
    ) {
        div {
            a {
                href = "/url"
                title = "\ntitle\nline1\nline2\n"
                +"foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample197() = doTest(
        markdown = "[foo]: /url 'title\n\nwith blank line'\n\n[foo]\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            +":"
            +" "
            +"/url"
            +" "
            +"'"
            +"title"
        }
        div {
            +"with blank line"
            +"'"
        }
        div {
            +"["
            +"foo"
            +"]"
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample198() = doTest(
        markdown = "[foo]:\n/url\n\n[foo]\n",
    ) {
        div {
            a {
                href = "/url"
                +"foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample199() = doTest(
        markdown = "[foo]:\n\n[foo]\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            +":"
        }
        div {
            +"["
            +"foo"
            +"]"
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample200() = doTest(
        markdown = "[foo]: <>\n\n[foo]\n",
    ) {
        div {
            a {
                href = ""
                +"foo"
            }
        }
    }

    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample201() = doTest(
        markdown = "[foo]: <bar>(baz)\n\n[foo]\n",
    ) {
        div {
            +"[foo]: <bar>(baz)</p>\\n<p>[foo]"
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample202() = doTest(
        markdown = "[foo]: /url\\bar\\*baz \"foo\\\"bar\\baz\"\n\n[foo]\n",
    ) {
        div {
            a {
                href = "/url%5Cbar*baz"
                title = "foo&quot;bar\\baz"
                +"foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample203() = doTest(
        markdown = "[foo]\n\n[foo]: url\n",
    ) {
        div {
            a {
                href = "url"
                +"foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample204() = doTest(
        markdown = "[foo]\n\n[foo]: first\n[foo]: second\n",
    ) {
        div {
            a {
                href = "first"
                +"foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample205() = doTest(
        markdown = "[FOO]: /url\n\n[Foo]\n",
    ) {
        div {
            a {
                href = "/url"
                +"Foo"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample206() = doTest(
        markdown = "[ΑΓΩ]: /φου\n\n[αγω]\n",
    ) {
        div {
            a {
                href = "/%CF%86%CE%BF%CF%85"
                +"αγω"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample207() = doTest(
        markdown = "[foo]: /url\n",
    ) {}

    @Test
    fun testLinkReferenceDefinitionsExample208() = doTest(
        markdown = "[\nfoo\n]: /url\nbar\n",
    ) {
        div {
            +"bar"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinkReferenceDefinitionsExample209() = doTest(
        markdown = "[foo]: /url \"title\" ok\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            +":"
            +" "
            +"/url"
            +" "
            +"\""
            +"title"
            +"\""
            +" "
            +"ok"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinkReferenceDefinitionsExample210() = doTest(
        markdown = "[foo]: /url\n\"title\" ok\n",
    ) {
        div {
            +"\""
            +"title"
            +"\""
            +" "
            +"ok"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinkReferenceDefinitionsExample211() = doTest(
        markdown = "    [foo]: /url \"title\"\n\n[foo]\n",
    ) {
        pre {
            code {
                +"[foo]: /url \"title\""
                +"\n"
            }
        }
        div {
            +"["
            +"foo"
            +"]"
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testLinkReferenceDefinitionsExample212() = doTest(
        markdown = "```\n[foo]: /url\n```\n\n[foo]\n",
    ) {
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
                +"[foo]: /url"
                +"\n"
            }
        }
        div {
            +"["
            +"foo"
            +"]"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testLinkReferenceDefinitionsExample213() = doTest(
        markdown = "Foo\n[bar]: /baz\n\n[bar]\n",
    ) {
        div {
            +"Foo"
            +"\n"
            br()
            +"["
            +"bar"
            +"]"
            +":"
            +" "
            +"/baz"
        }
        div {
            +"["
            +"bar"
            +"]"
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample214() = doTest(
        markdown = "# [Foo]\n[foo]: /url\n> bar\n",
    ) {
        h1 {
            a {
                href = "/url"
                +"Foo"
            }
        }
        blockquote {
            div {
                +"bar"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample215() = doTest(
        markdown = "[foo]: /url\nbar\n===\n[foo]\n",
    ) {
        h1 {
            +"bar"
        }
        div {
            a {
                href = "/url"
                +"foo"
            }
        }
    }

    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample216() = doTest(
        markdown = "[foo]: /url\n===\n[foo]\n",
    ) {
        div {
            +"==="
            a {
                href = "/url"
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testLinkReferenceDefinitionsExample217() = doTest(
        markdown = "[foo]: /foo-url \"foo\"\n[bar]: /bar-url\n  \"bar\"\n[baz]: /baz-url\n\n[foo],\n[bar],\n[baz]\n",
    ) {
        div {
            a {
                href = "/foo-url"
                title = "foo"
                +"foo"
            }
            +","
            +"\n"
            br()
            a {
                href = "/bar-url"
                title = "bar"
                +"bar"
            }
            +","
            +"\n"
            br()
            a {
                href = "/baz-url"
                +"baz"
            }
        }
    }

    @Test
    fun testLinkReferenceDefinitionsExample218() = doTest(
        markdown = "[foo]\n\n> [foo]: /url\n",
    ) {
        div {
            a {
                href = "/url"
                +"foo"
            }
        }
        blockquote()
    }

    @Test
    fun testParagraphsExample219() = doTest(
        markdown = "aaa\n\nbbb\n",
    ) {
        div {
            +"aaa"
        }
        div {
            +"bbb"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testParagraphsExample220() = doTest(
        markdown = "aaa\nbbb\n\nccc\nddd\n",
    ) {
        div {
            +"aaa"
            +"\n"
            br()
            +"bbb"
        }
        div {
            +"ccc"
            +"\n"
            br()
            +"ddd"
        }
    }

    @Test
    fun testParagraphsExample221() = doTest(
        markdown = "aaa\n\n\nbbb\n",
    ) {
        div {
            +"aaa"
        }
        div {
            +"bbb"
        }
    }

    @Test
    @Ignore
    fun testParagraphsExample222() = doTest(
        markdown = "  aaa\n bbb\n",
    ) {
        div {
            +"aaa"
            +"\n"
            +"bbb"
        }
    }

    @Test
    @Ignore
    fun testParagraphsExample223() = doTest(
        markdown = "aaa\n             bbb\n                                       ccc\n",
    ) {
        div {
            +"aaa"
            +"\n"
            +"bbb"
            +"\n"
            +"ccc"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testParagraphsExample224() = doTest(
        markdown = "   aaa\nbbb\n",
    ) {
        div {
            +"aaa"
            +"\n"
            br()
            +"bbb"
        }
    }

    @Test
    fun testParagraphsExample225() = doTest(
        markdown = "    aaa\nbbb\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
            }
        }
        div {
            +"bbb"
        }
    }

    @Test
    fun testParagraphsExample226() = doTest(
        markdown = "aaa     \nbbb     \n",
    ) {
        div {
            +"aaa"
            br()
            +"\n"
            +"bbb"
        }
    }

    @Test
    fun testBlankLinesExample227() = doTest(
        markdown = "  \n\naaa\n  \n\n# aaa\n\n  \n",
    ) {
        div {
            +"aaa"
        }
        h1 {
            +"aaa"
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample228() = doTest(
        markdown = "> # Foo\n> bar\n> baz\n",
    ) {
        blockquote {
            h1 {
                +"Foo"
            }
            div {
                +"bar"
                +"\n"
                +"baz"
            }
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample229() = doTest(
        markdown = "># Foo\n>bar\n> baz\n",
    ) {
        blockquote {
            h1 {
                +"Foo"
            }
            div {
                +"bar"
                +"\n"
                +"baz"
            }
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample230() = doTest(
        markdown = "   > # Foo\n   > bar\n > baz\n",
    ) {
        blockquote {
            h1 {
                +"Foo"
            }
            div {
                +"bar"
                +"\n"
                +"baz"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testBlockQuotesExample231() = doTest(
        markdown = "    > # Foo\n    > bar\n    > baz\n",
    ) {
        pre {
            code {
                +"> # Foo"
                +"\n"
                +"> bar"
                +"\n"
                +"> baz"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testBlockQuotesExample232() = doTest(
        markdown = "> # Foo\n> bar\nbaz\n",
    ) {
        blockquote {
            h1 {
                +"Foo"
            }
            div {
                +"bar"
                +"\n"
                br()
                +"baz"
            }
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample233() = doTest(
        markdown = "> bar\nbaz\n> foo\n",
    ) {
        blockquote {
            div {
                +"bar"
                +"\n"
                +"baz"
                +"\n"
                +"foo"
            }
        }
    }

    @Test
    fun testBlockQuotesExample234() = doTest(
        markdown = "> foo\n---\n",
    ) {
        blockquote {
            div {
                +"foo"
            }
        }
        hr()
    }

    @Test
    fun testBlockQuotesExample235() = doTest(
        markdown = "> - foo\n- bar\n",
    ) {
        blockquote {
            ul {
                li {
                    +"foo"
                }
            }
        }
        ul {
            li {
                +"bar"
            }
        }
    }

    @Test
    fun testBlockQuotesExample236() = doTest(
        markdown = ">     foo\n    bar\n",
    ) {
        blockquote {
            pre {
                code {
                    +"foo"
                    +"\n"
                }
            }
        }
        pre {
            code {
                +"bar"
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testBlockQuotesExample237() = doTest(
        markdown = "> ```\nfoo\n```\n",
    ) {
        blockquote {
            pre {
                className = ClassName("language-")
                code {
                    className = ClassName("language-")
                }
            }
        }
        div {
            +"foo"
        }
        pre {
            className = ClassName("language-")
            code {
                className = ClassName("language-")
            }
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample238() = doTest(
        markdown = "> foo\n    - bar\n",
    ) {
        blockquote {
            div {
                +"foo\n- bar"
            }
        }
    }

    @Test
    fun testBlockQuotesExample239() = doTest(
        markdown = ">\n",
    ) {
        blockquote()
    }

    @Test
    fun testBlockQuotesExample240() = doTest(
        markdown = ">\n>  \n> \n",
    ) {
        blockquote()
    }

    @Test
    fun testBlockQuotesExample241() = doTest(
        markdown = ">\n> foo\n>  \n",
    ) {
        blockquote {
            div {
                +"foo"
            }
        }
    }

    @Test
    fun testBlockQuotesExample242() = doTest(
        markdown = "> foo\n\n> bar\n",
    ) {
        blockquote {
            div {
                +"foo"
            }
        }
        blockquote {
            div {
                +"bar"
            }
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample243() = doTest(
        markdown = "> foo\n> bar\n",
    ) {
        blockquote {
            div {
                +"foo"
            }
            div {
                +"bar"
            }
        }
    }

    @Test
    fun testBlockQuotesExample244() = doTest(
        markdown = "> foo\n>\n> bar\n",
    ) {
        blockquote {
            div {
                +"foo"
            }
            div {
                +"bar"
            }
        }
    }

    @Test
    fun testBlockQuotesExample245() = doTest(
        markdown = "foo\n> bar\n",
    ) {
        div {
            +"foo"
        }
        blockquote {
            div {
                +"bar"
            }
        }
    }

    @Test
    fun testBlockQuotesExample246() = doTest(
        markdown = "> aaa\n***\n> bbb\n",
    ) {
        blockquote {
            div {
                +"aaa"
            }
        }
        hr()
        blockquote {
            div {
                +"bbb"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testBlockQuotesExample247() = doTest(
        markdown = "> bar\nbaz\n",
    ) {
        blockquote {
            div {
                +"bar"
                +"\n"
                br()
                +"baz"
            }
        }
    }

    @Test
    fun testBlockQuotesExample248() = doTest(
        markdown = "> bar\n\nbaz\n",
    ) {
        blockquote {
            div {
                +"bar"
            }
        }
        div {
            +"baz"
        }
    }

    @Test
    fun testBlockQuotesExample249() = doTest(
        markdown = "> bar\n>\nbaz\n",
    ) {
        blockquote {
            div {
                +"bar"
            }
        }
        div {
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testBlockQuotesExample250() = doTest(
        markdown = "> > > foo\nbar\n",
    ) {
        blockquote {
            blockquote {
                blockquote {
                    div {
                        +"foo"
                        +"\n"
                        br()
                        +"bar"
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testBlockQuotesExample251() = doTest(
        markdown = ">>> foo\n> bar\n>>baz\n",
    ) {
        blockquote {
            blockquote {
                blockquote {
                    div {
                        +"foo"
                        +"\n"
                        +"bar"
                        +"\n"
                        +"baz"
                    }
                }
            }
        }
    }

    @Test
    fun testBlockQuotesExample252() = doTest(
        markdown = ">     code\n\n>    not code\n",
    ) {
        blockquote {
            pre {
                code {
                    +"code"
                    +"\n"
                }
            }
        }
        blockquote {
            div {
                +"not code"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testListItemsExample253() = doTest(
        markdown = "A paragraph\nwith two lines.\n\n    indented code\n\n> A block quote.\n",
    ) {
        div {
            +"A paragraph"
            +"\n"
            br()
            +"with two lines."
        }
        pre {
            code {
                +"indented code"
                +"\n"
            }
        }
        blockquote {
            div {
                +"A block quote."
            }
        }
    }

    @Test
    @Ignore
    fun testListItemsExample254() = doTest(
        markdown = "1.  A paragraph\n    with two lines.\n\n        indented code\n\n    > A block quote.\n",
    ) {
        ol {
            li {
                div {
                    +"A paragraph"
                    +"\n"
                    +"with no lines."
                }
                pre {
                    code {
                        +"indented code"
                        +"\n"
                    }
                }
                blockquote {
                    div {
                        +"A block quote."
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample255() = doTest(
        markdown = "- one\n\n two\n",
    ) {
        ul {
            li {
                +"one"
            }
        }
        div {
            +"two"
        }
    }

    @Test
    fun testListItemsExample256() = doTest(
        markdown = "- one\n\n  two\n",
    ) {
        ul {
            li {
                div {
                    +"one"
                }
                div {
                    +"two"
                }
            }
        }
    }

    @Test
    fun testListItemsExample257() = doTest(
        markdown = " -    one\n\n     two\n",
    ) {
        ul {
            li {
                +"one"
            }
        }
        pre {
            code {
                +" two"
                +"\n"
            }
        }
    }

    @Test
    fun testListItemsExample258() = doTest(
        markdown = " -    one\n\n      two\n",
    ) {
        ul {
            li {
                div {
                    +"one"
                }
                div {
                    +"two"
                }
            }
        }
    }

    @Test
    fun testListItemsExample259() = doTest(
        markdown = "   > > 1.  one\n>>\n>>     two\n",
    ) {
        blockquote {
            blockquote {
                ol {
                    li {
                        div {
                            +"one"
                        }
                        div {
                            +"two"
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample260() = doTest(
        markdown = ">>- one\n>>\n  >  > two\n",
    ) {
        blockquote {
            blockquote {
                ul {
                    li {
                        +"one"
                    }
                }
                div {
                    +"two"
                }
            }
        }
    }

    @Test
    fun testListItemsExample261() = doTest(
        markdown = "-one\n\n2.two\n",
    ) {
        div {
            +"-one"
        }
        div {
            +"2.two"
        }
    }

    @Test
    @Ignore
    fun testListItemsExample262() = doTest(
        markdown = "- foo\n\n\n  bar\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
                div {
                    +"bar"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testListItemsExample263() = doTest(
        markdown = "1.  foo\n\n    ```\n    bar\n    ```\n\n    baz\n\n    > bam\n",
    ) {
        ol {
            li {
                div {
                    +"foo"
                }
                pre {
                    className = ClassName("language-")
                    code {
                        className = ClassName("language-")
                        +"bar"
                        +"\n"
                    }
                }
                div {
                    +"baz"
                }
                blockquote {
                    div {
                        +"bam"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample264() = doTest(
        markdown = "- Foo\n\n      bar\n\n\n      baz\n",
    ) {
        ul {
            li {
                div {
                    +"Foo"
                }
                pre {
                    code {
                        +"bar"
                        +"\n"
                        +"\n"
                        +"\n"
                        +"baz"
                        +"\n"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample265() = doTest(
        markdown = "123456789. ok\n",
    ) {
        ol {
            start = 123456789
            li {
                +"ok"
            }
        }
    }

    @Test
    fun testListItemsExample266() = doTest(
        markdown = "1234567890. not ok\n",
    ) {
        div {
            +"1234567890."
            +" "
            +"not ok"
        }
    }

    @Test
    fun testListItemsExample267() = doTest(
        markdown = "0. ok\n",
    ) {
        ol {
            start = 0
            li {
                +"ok"
            }
        }
    }

    @Test
    fun testListItemsExample268() = doTest(
        markdown = "003. ok\n",
    ) {
        ol {
            start = 3
            li {
                +"ok"
            }
        }
    }

    @Test
    fun testListItemsExample269() = doTest(
        markdown = "-1. not ok\n",
    ) {
        div {
            +"-1."
            +" "
            +"not ok"
        }
    }

    @Test
    fun testListItemsExample270() = doTest(
        markdown = "- foo\n\n      bar\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
                pre {
                    code {
                        +"bar"
                        +"\n"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample271() = doTest(
        markdown = "  10.  foo\n\n           bar\n",
    ) {
        ol {
            start = 10
            li {
                div {
                    +"foo"
                }
                pre {
                    code {
                        +"bar"
                        +"\n"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample272() = doTest(
        markdown = "    indented code\n\nparagraph\n\n    more code\n",
    ) {
        pre {
            code {
                +"indented code"
                +"\n"
            }
        }
        div {
            +"paragraph"
        }
        pre {
            code {
                +"more code"
                +"\n"
            }
        }
    }

    @Test
    fun testListItemsExample273() = doTest(
        markdown = "1.     indented code\n\n   paragraph\n\n       more code\n",
    ) {
        ol {
            li {
                pre {
                    code {
                        +"indented code"
                        +"\n"
                    }
                }
                div {
                    +"paragraph"
                }
                pre {
                    code {
                        +"more code"
                        +"\n"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample274() = doTest(
        markdown = "1.      indented code\n\n   paragraph\n\n       more code\n",
    ) {
        ol {
            li {
                pre {
                    code {
                        +" indented code"
                        +"\n"
                    }
                }
                div {
                    +"paragraph"
                }
                pre {
                    code {
                        +"more code"
                        +"\n"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample275() = doTest(
        markdown = "   foo\n\nbar\n",
    ) {
        div {
            +"foo"
        }
        div {
            +"bar"
        }
    }

    @Test
    fun testListItemsExample276() = doTest(
        markdown = "-    foo\n\n  bar\n",
    ) {
        ul {
            li {
                +"foo"
            }
        }
        div {
            +"bar"
        }
    }

    @Test
    fun testListItemsExample277() = doTest(
        markdown = "-  foo\n\n   bar\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
                div {
                    +"bar"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testListItemsExample278() = doTest(
        markdown = "-\n  foo\n-\n  ```\n  bar\n  ```\n-\n      baz\n",
    ) {
        ul {
            li {
                +"foo"
            }
            li {
                pre {
                    className = ClassName("language-")
                    code {
                        className = ClassName("language-")
                        +"bar"
                        +"\n"
                    }
                }
            }
            li {
                pre {
                    code {
                        +"baz"
                        +"\n"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample279() = doTest(
        markdown = "-   \n  foo\n",
    ) {
        ul {
            li {
                +"foo"
            }
        }
    }

    @Test
    @Ignore
    fun testListItemsExample280() = doTest(
        markdown = "-\n\n  foo\n",
    ) {
        ul {
            li()
        }
        div {
            +"foo"
        }
    }

    @Test
    fun testListItemsExample281() = doTest(
        markdown = "- foo\n-\n- bar\n",
    ) {
        ul {
            li {
                +"foo"
            }
            li()
            li {
                +"bar"
            }
        }
    }

    @Test
    fun testListItemsExample282() = doTest(
        markdown = "- foo\n-   \n- bar\n",
    ) {
        ul {
            li {
                +"foo"
            }
            li()
            li {
                +"bar"
            }
        }
    }

    @Test
    fun testListItemsExample283() = doTest(
        markdown = "1. foo\n2.\n3. bar\n",
    ) {
        ol {
            li {
                +"foo"
            }
            li()
            li {
                +"bar"
            }
        }
    }

    @Test
    fun testListItemsExample284() = doTest(
        markdown = "*\n",
    ) {
        ul {
            li()
        }
    }

    @Test
    @Ignore
    fun testListItemsExample285() = doTest(
        markdown = "foo\n*\n\nfoo\n1.\n",
    ) {
        div {
            +"foo"
            +"\n"
            +"*"
        }
        div {
            +"foo"
            +"\n"
            +"1."
        }
    }

    @Test
    @Ignore
    fun testListItemsExample286() = doTest(
        markdown = " 1.  A paragraph\n     with two lines.\n\n         indented code\n\n     > A block quote.\n",
    ) {
        ol {
            li {
                div {
                    +"A paragraph"
                    +"\n"
                    +"with two lines"
                }
                pre {
                    code {
                        +"indented code"
                    }
                }
                blockquote {
                    div {
                        +"A block quote."
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testListItemsExample287() = doTest(
        markdown = "  1.  A paragraph\n      with two lines.\n\n          indented code\n\n      > A block quote.\n",
    ) {
        ol {
            li {
                div {
                    +"A parapraph"
                    +"\n"
                    +"with two lines."
                }
                pre {
                    code {
                        +"indented code"
                    }
                }
                blockquote {
                    div {
                        +"A block quote."
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testListItemsExample288() = doTest(
        markdown = "   1.  A paragraph\n       with two lines.\n\n           indented code\n\n       > A block quote.\n",
    ) {
        ol {
            li {
                div {
                    +"A paragraph"
                    +"\n"
                    +"with two lines."
                }
                pre {
                    code {
                        +"indented code"
                        +"\n"
                    }
                }
                blockquote {
                    div {
                        +"A block quote."
                    }
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testListItemsExample289() = doTest(
        markdown = "    1.  A paragraph\n        with two lines.\n\n            indented code\n\n        > A block quote.\n",
    ) {
        pre {
            code {
                +"1.  A paragraph"
                +"\n"
                +"    with two lines."
                +"\n"
                +"\n"
                +"        indented code"
                +"\n"
                +"\n"
                +"    > A block quote."
                +"\n"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testListItemsExample290() = doTest(
        markdown = "  1.  A paragraph\nwith two lines.\n\n          indented code\n\n      > A block quote.\n",
    ) {
        ol {
            li {
                div {
                    +"A paragraph"
                    +"\n"
                    br()
                    +"with two lines."
                }
                pre {
                    code {
                        +"indented code"
                        +"\n"
                    }
                }
                blockquote {
                    div {
                        +"A block quote."
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testListItemsExample291() = doTest(
        markdown = "  1.  A paragraph\n    with two lines.\n",
    ) {
        ol {
            li {
                +"A parapraph"
                +"\n"
                +"with two lines."
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testListItemsExample292() = doTest(
        markdown = "> 1. > Blockquote\ncontinued here.\n",
    ) {
        blockquote {
            ol {
                li {
                    blockquote {
                        div {
                            +"Blockquote"
                            +"\n"
                            br()
                            +"continued here."
                        }
                    }
                }
            }
        }
    }

    @Test
    @Ignore
    fun testListItemsExample293() = doTest(
        markdown = "> 1. > Blockquote\n> continued here.\n",
    ) {
        blockquote {
            ol {
                li {
                    blockquote {
                        div {
                            +"Blockquote"
                            +"\n"
                            +"continued here."
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample294() = doTest(
        markdown = "- foo\n  - bar\n    - baz\n      - boo\n",
    ) {
        ul {
            li {
                +"foo"
                ul {
                    li {
                        +"bar"
                        ul {
                            li {
                                +"baz"
                                ul {
                                    li {
                                        +"boo"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample295() = doTest(
        markdown = "- foo\n - bar\n  - baz\n   - boo\n",
    ) {
        ul {
            li {
                +"foo"
            }
            li {
                +"bar"
            }
            li {
                +"baz"
            }
            li {
                +"boo"
            }
        }
    }

    @Test
    fun testListItemsExample296() = doTest(
        markdown = "10) foo\n    - bar\n",
    ) {
        ol {
            start = 10
            li {
                +"foo"
                ul {
                    li {
                        +"bar"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample297() = doTest(
        markdown = "10) foo\n   - bar\n",
    ) {
        ol {
            start = 10
            li {
                +"foo"
            }
        }
        ul {
            li {
                +"bar"
            }
        }
    }

    @Test
    fun testListItemsExample298() = doTest(
        markdown = "- - foo\n",
    ) {
        ul {
            li {
                ul {
                    li {
                        +"foo"
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample299() = doTest(
        markdown = "1. - 2. foo\n",
    ) {
        ol {
            li {
                ul {
                    li {
                        ol {
                            start = 2
                            li {
                                +"foo"
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testListItemsExample300() = doTest(
        markdown = "- # Foo\n- Bar\n  ---\n  baz\n",
    ) {
        ul {
            li {
                h1 {
                    +"Foo"
                }
            }
            li {
                h2 {
                    +"Bar"
                }
                +"baz"
            }
        }
    }

    @Test
    fun testListsExample301() = doTest(
        markdown = "- foo\n- bar\n+ baz\n",
    ) {
        ul {
            li {
                +"foo"
            }
            li {
                +"bar"
            }
        }
        ul {
            li {
                +"baz"
            }
        }
    }

    @Test
    fun testListsExample302() = doTest(
        markdown = "1. foo\n2. bar\n3) baz\n",
    ) {
        ol {
            li {
                +"foo"
            }
            li {
                +"bar"
            }
        }
        ol {
            start = 3
            li {
                +"baz"
            }
        }
    }

    @Test
    fun testListsExample303() = doTest(
        markdown = "Foo\n- bar\n- baz\n",
    ) {
        div {
            +"Foo"
        }
        ul {
            li {
                +"bar"
            }
            li {
                +"baz"
            }
        }
    }

    @Test
    @Ignore
    fun testListsExample304() = doTest(
        markdown = "The number of windows in my house is\n14.  The number of doors is 6.\n",
    ) {
        div {
            +"The number of windows in my house is"
            +"\n"
            +"14.  The number of doors is 6."
        }
    }

    @Test
    fun testListsExample305() = doTest(
        markdown = "The number of windows in my house is\n1.  The number of doors is 6.\n",
    ) {
        div {
            +"The number of windows in my house is"
        }
        ol {
            li {
                +"The number of doors is 6."
            }
        }
    }

    @Test
    @Ignore
    fun testListsExample306() = doTest(
        markdown = "- foo\n\n- bar\n\n\n- baz\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
            }
            li {
                div {
                    +"bar"
                }
            }
            li {
                div {
                    +"baz"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testListsExample307() = doTest(
        markdown = "- foo\n  - bar\n    - baz\n\n\n      bim\n",
    ) {
        ul {
            li {
                +"foo"
                ul {
                    li {
                        +"bar\n"
                        ul {
                            li {
                                div {
                                    +"baz"
                                }
                                div {
                                    +"bim"
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testListsExample308() = doTest(
        markdown = "- foo\n- bar\n\n<!-- -->\n\n- baz\n- bim\n",
    ) {
        ul {
            li {
                +"foo"
            }
            li {
                +"bar"
            }
        }
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<!-- -->"
            }
        }
        ul {
            li {
                +"baz"
            }
            li {
                +"bim"
            }
        }
    }

    @Test
    fun testListsExample309() = doTest(
        markdown = "-   foo\n\n    notcode\n\n-   foo\n\n<!-- -->\n\n    code\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
                div {
                    +"notcode"
                }
            }
            li {
                div {
                    +"foo"
                }
            }
        }
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<!-- -->"
            }
        }
        pre {
            code {
                +"code"
                +"\n"
            }
        }
    }

    @Test
    fun testListsExample310() = doTest(
        markdown = "- a\n - b\n  - c\n   - d\n  - e\n - f\n- g\n",
    ) {
        ul {
            li {
                +"a"
            }
            li {
                +"b"
            }
            li {
                +"c"
            }
            li {
                +"d"
            }
            li {
                +"e"
            }
            li {
                +"f"
            }
            li {
                +"g"
            }
        }
    }

    @Test
    fun testListsExample311() = doTest(
        markdown = "1. a\n\n  2. b\n\n   3. c\n",
    ) {
        ol {
            li {
                div {
                    +"a"
                }
            }
            li {
                div {
                    +"b"
                }
            }
            li {
                div {
                    +"c"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testListsExample312() = doTest(
        markdown = "- a\n - b\n  - c\n   - d\n    - e\n",
    ) {
        ul {
            li {
                +"a"
            }
            li {
                +"b"
            }
            li {
                +"c"
            }
            li {
                +"d\n- e"
            }
        }
    }

    @Test
    @Ignore
    fun testListsExample313() = doTest(
        markdown = "1. a\n\n  2. b\n\n    3. c\n",
    ) {
        ol {
            li {
                div {
                    +"a"
                }
            }
            li {
                div {
                    +"b"
                }
            }
        }
        pre {
            code {
                +"3. c"
                +"\n"
            }
        }
    }

    @Test
    fun testListsExample314() = doTest(
        markdown = "- a\n- b\n\n- c\n",
    ) {
        ul {
            li {
                div {
                    +"a"
                }
            }
            li {
                div {
                    +"b"
                }
            }
            li {
                div {
                    +"c"
                }
            }
        }
    }

    @Test
    fun testListsExample315() = doTest(
        markdown = "* a\n*\n\n* c\n",
    ) {
        ul {
            li {
                div {
                    +"a"
                }
            }
            li()
            li {
                div {
                    +"c"
                }
            }
        }
    }

    @Test
    fun testListsExample316() = doTest(
        markdown = "- a\n- b\n\n  c\n- d\n",
    ) {
        ul {
            li {
                div {
                    +"a"
                }
            }
            li {
                div {
                    +"b"
                }
                div {
                    +"c"
                }
            }
            li {
                div {
                    +"d"
                }
            }
        }
    }

    @Test
    fun testListsExample317() = doTest(
        markdown = "- a\n- b\n\n  [ref]: /url\n- d\n",
    ) {
        ul {
            li {
                div {
                    +"a"
                }
            }
            li {
                div {
                    +"b"
                }
            }
            li {
                div {
                    +"d"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testListsExample318() = doTest(
        markdown = "- a\n- ```\n  b\n\n\n  ```\n- c\n",
    ) {
        ul {
            li {
                +"a"
            }
            li {
                pre {
                    className = ClassName("language-")
                    code {
                        className = ClassName("language-")
                        +"b"
                        +"\n"
                        +"\n"
                        +"\n"
                    }
                }
            }
            li {
                +"c"
            }
        }
    }

    @Test
    fun testListsExample319() = doTest(
        markdown = "- a\n  - b\n\n    c\n- d\n",
    ) {
        ul {
            li {
                +"a"
                ul {
                    li {
                        div {
                            +"b"
                        }
                        div {
                            +"c"
                        }
                    }
                }
            }
            li {
                +"d"
            }
        }
    }

    @Test
    fun testListsExample320() = doTest(
        markdown = "* a\n  > b\n  >\n* c\n",
    ) {
        ul {
            li {
                +"a"
                blockquote {
                    div {
                        +"b"
                    }
                }
            }
            li {
                +"c"
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testListsExample321() = doTest(
        markdown = "- a\n  > b\n  ```\n  c\n  ```\n- d\n",
    ) {
        ul {
            li {
                +"a"
                blockquote {
                    div {
                        +"b"
                    }
                }
                pre {
                    className = ClassName("language-")
                    code {
                        className = ClassName("language-")
                        +"c"
                        +"\n"
                    }
                }
            }
            li {
                +"d"
            }
        }
    }

    @Test
    fun testListsExample322() = doTest(
        markdown = "- a\n",
    ) {
        ul {
            li {
                +"a"
            }
        }
    }

    @Test
    fun testListsExample323() = doTest(
        markdown = "- a\n  - b\n",
    ) {
        ul {
            li {
                +"a"
                ul {
                    li {
                        +"b"
                    }
                }
            }
        }
    }

    /**
     * Modified
     * Reason: By changing to attach a language information class to the code fence.
     */
    @Test
    fun testListsExample324() = doTest(
        markdown = "1. ```\n   foo\n   ```\n\n   bar\n",
    ) {
        ol {
            li {
                pre {
                    className = ClassName("language-")
                    code {
                        className = ClassName("language-")
                        +"foo"
                        +"\n"
                    }
                }
                div {
                    +"bar"
                }
            }
        }
    }

    @Test
    fun testListsExample325() = doTest(
        markdown = "* foo\n  * bar\n\n  baz\n",
    ) {
        ul {
            li {
                div {
                    +"foo"
                }
                ul {
                    li {
                        +"bar"
                    }
                }
                div {
                    +"baz"
                }
            }
        }
    }

    @Test
    fun testListsExample326() = doTest(
        markdown = "- a\n  - b\n  - c\n\n- d\n  - e\n  - f\n",
    ) {
        ul {
            li {
                div {
                    +"a"
                }
                ul {
                    li {
                        +"b"
                    }
                    li {
                        +"c"
                    }
                }
            }
            li {
                div {
                    +"d"
                }
                ul {
                    li {
                        +"e"
                    }
                    li {
                        +"f"
                    }
                }
            }
        }
    }

    @Test
    fun testInlinesExample327() = doTest(
        markdown = "`hi`lo`\n",
    ) {
        div {
            code {
                +"hi"
            }
            +"lo"
            +"`"
        }
    }

    @Test
    fun testCodeSpansExample328() = doTest(
        markdown = "`foo`\n",
    ) {
        div {
            code {
                +"foo"
            }
        }
    }

    @Test
    fun testCodeSpansExample329() = doTest(
        markdown = "`` foo ` bar ``\n",
    ) {
        div {
            code {
                +"foo ` bar"
            }
        }
    }

    @Test
    fun testCodeSpansExample330() = doTest(
        markdown = "` `` `\n",
    ) {
        div {
            code {
                +"``"
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample331() = doTest(
        markdown = "`  ``  `\n",
    ) {
        div {
            code {
                +" `` "
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample332() = doTest(
        markdown = "` a`\n",
    ) {
        div {
            code {
                +" a"
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample333() = doTest(
        markdown = "` b `\n",
    ) {
        div {
            code {
                +" b "
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample334() = doTest(
        markdown = "` `\n`  `\n",
    ) {
        div {
            code {
                +" "
            }
            code {
                +"  "
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample335() = doTest(
        markdown = "``\nfoo\nbar  \nbaz\n``\n",
    ) {
        div {
            code {
                +"foo bar   baz"
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample336() = doTest(
        markdown = "``\nfoo \n``\n",
    ) {
        div {
            code {
                +"foo "
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample337() = doTest(
        markdown = "`foo   bar \nbaz`\n",
    ) {
        div {
            code {
                +"foo   bar  baz"
            }
        }
    }

    @Test
    @Ignore
    fun testCodeSpansExample338() = doTest(
        markdown = "`foo\\`bar`\n",
    ) {
        div {
            code {
                +"foo\\"
            }
            +"bar`"
        }
    }

    @Test
    fun testCodeSpansExample339() = doTest(
        markdown = "``foo`bar``\n",
    ) {
        div {
            code {
                +"foo`bar"
            }
        }
    }

    @Test
    fun testCodeSpansExample340() = doTest(
        markdown = "` foo `` bar `\n",
    ) {
        div {
            code {
                +"foo `` bar"
            }
        }
    }

    @Test
    fun testCodeSpansExample341() = doTest(
        markdown = "*foo`*`\n",
    ) {
        div {
            +"*"
            +"foo"
            code {
                +"*"
            }
        }
    }

    @Test
    fun testCodeSpansExample342() = doTest(
        markdown = "[not a `link](/foo`)\n",
    ) {
        div {
            +"["
            +"not a"
            +" "
            code {
                +"link](/foo"
            }
            +")"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testCodeSpansExample343() = doTest(
        markdown = "`<a href=\"`\">`\n",
    ) {
        div {
            code {
                +"<a href=\""
            }
            +"\">`"
        }
    }

    @Test
    fun testCodeSpansExample344() = doTest(
        markdown = "<a href=\"`\">`\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"`\">"
                }
            }
            +"`"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testCodeSpansExample345() = doTest(
        markdown = "`<http://foo.bar.`baz>`\n",
    ) {
        div {
            code {
                +"<http://foo.bar."
            }
            +"baz>`"
        }
    }

    @Test
    fun testCodeSpansExample346() = doTest(
        markdown = "<http://foo.bar.`baz>`\n",
    ) {
        div {
            a {
                href = "http://foo.bar.%60baz"
                +"http://foo.bar.`baz"
            }
            +"`"
        }
    }

    @Test
    fun testCodeSpansExample347() = doTest(
        markdown = "```foo``\n",
    ) {
        div {
            +"```"
            +"foo"
            +"``"
        }
    }

    @Test
    fun testCodeSpansExample348() = doTest(
        markdown = "`foo\n",
    ) {
        div {
            +"`"
            +"foo"
        }
    }

    @Test
    fun testCodeSpansExample349() = doTest(
        markdown = "`foo``bar``\n",
    ) {
        div {
            +"`"
            +"foo"
            code {
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample350() = doTest(
        markdown = "*foo bar*\n",
    ) {
        div {
            em {
                +"foo bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample351() = doTest(
        markdown = "a * foo bar*\n",
    ) {
        div {
            +"a * foo bar"
            +"*"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample352() = doTest(
        markdown = "a*\"foo\"*\n",
    ) {
        div {
            +"a"
            +"*"
            +"\""
            +"foo"
            +"\""
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample353() = doTest(
        markdown = "* a *\n",
    ) {
        div {
            +"*"
            +" a "
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample354() = doTest(
        markdown = "foo*bar*\n",
    ) {
        div {
            +"foo"
            em {
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample355() = doTest(
        markdown = "5*6*78\n",
    ) {
        div {
            +"5"
            em {
                +"6"
            }
            +"78"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample356() = doTest(
        markdown = "_foo bar_\n",
    ) {
        div {
            em {
                +"foo bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample357() = doTest(
        markdown = "_ foo bar_\n",
    ) {
        div {
            +"_"
            +" "
            +"foo bar"
            +"_"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample358() = doTest(
        markdown = "a_\"foo\"_\n",
    ) {
        div {
            +"a"
            +"_"
            +"\""
            +"foo"
            +"\""
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample359() = doTest(
        markdown = "foo_bar_\n",
    ) {
        div {
            +"foo_bar"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample360() = doTest(
        markdown = "5_6_78\n",
    ) {
        div {
            +"5_6_78"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample361() = doTest(
        markdown = "пристаням_стремятся_\n",
    ) {
        div {
            +"пристаням_стремятся"
            +"_"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample362() = doTest(
        markdown = "aa_\"bb\"_cc\n",
    ) {
        div {
            +"aa"
            +"_"
            +"\""
            +"bb"
            +"\""
            +"_"
            +"cc"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample363() = doTest(
        markdown = "foo-_(bar)_\n",
    ) {
        div {
            +"foo-"
            em {
                +"("
                +"bar"
                +")"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample364() = doTest(
        markdown = "_foo*\n",
    ) {
        div {
            +"_"
            +"foo"
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample365() = doTest(
        markdown = "*foo bar *\n",
    ) {
        div {
            +"*"
            +"foo bar"
            +" "
            +"*"
        }
    }

    @Test
    @Ignore
    fun testEmphasisAndStrongEmphasisExample366() = doTest(
        markdown = "*foo bar\n*\n",
    ) {
        div {
            +"*foo bar\n*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample367() = doTest(
        markdown = "*(*foo)\n",
    ) {
        div {
            +"*"
            +"("
            +"*"
            +"foo"
            +")"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample368() = doTest(
        markdown = "*(*foo*)*\n",
    ) {
        div {
            em {
                +"("
                em {
                    +"foo"
                }
                +")"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample369() = doTest(
        markdown = "*foo*bar\n",
    ) {
        div {
            em {
                +"foo"
            }
            +"bar"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample370() = doTest(
        markdown = "_foo bar _\n",
    ) {
        div {
            +"_"
            +"foo bar"
            +" "
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample371() = doTest(
        markdown = "_(_foo)\n",
    ) {
        div {
            +"_"
            +"("
            +"_"
            +"foo"
            +")"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample372() = doTest(
        markdown = "_(_foo_)_\n",
    ) {
        div {
            em {
                +"("
                em {
                    +"foo"
                }
                +")"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample373() = doTest(
        markdown = "_foo_bar\n",
    ) {
        div {
            +"_"
            +"foo_bar"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample374() = doTest(
        markdown = "_пристаням_стремятся\n",
    ) {
        div {
            +"_"
            +"пристаням_стремятся"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample375() = doTest(
        markdown = "_foo_bar_baz_\n",
    ) {
        div {
            em {
                +"foo_bar_baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample376() = doTest(
        markdown = "_(bar)_.\n",
    ) {
        div {
            em {
                +"("
                +"bar"
                +")"
            }
            +"."
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample377() = doTest(
        markdown = "**foo bar**\n",
    ) {
        div {
            strong {
                +"foo bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample378() = doTest(
        markdown = "** foo bar**\n",
    ) {
        div {
            +"*"
            +"*"
            +" "
            +"foo bar"
            +"*"
            +"*"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample379() = doTest(
        markdown = "a**\"foo\"**\n",
    ) {
        div {
            +"a"
            +"*"
            +"*"
            +"\""
            +"foo"
            +"\""
            +"*"
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample380() = doTest(
        markdown = "foo**bar**\n",
    ) {
        div {
            +"foo"
            strong {
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample381() = doTest(
        markdown = "__foo bar__\n",
    ) {
        div {
            strong {
                +"foo bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample382() = doTest(
        markdown = "__ foo bar__\n",
    ) {
        div {
            +"_"
            +"_"
            +" "
            +"foo bar"
            +"_"
            +"_"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample383() = doTest(
        markdown = "__\nfoo bar__\n",
    ) {
        div {
            +"_"
            +"_"
            +"\n"
            br()
            +"foo bar"
            +"_"
            +"_"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample384() = doTest(
        markdown = "a__\"foo\"__\n",
    ) {
        div {
            +"a"
            +"_"
            +"_"
            +"\""
            +"foo"
            +"\""
            +"_"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample385() = doTest(
        markdown = "foo__bar__\n",
    ) {
        div {
            +"foo__bar"
            +"_"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample386() = doTest(
        markdown = "5__6__78\n",
    ) {
        div {
            +"5__6__78"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample387() = doTest(
        markdown = "пристаням__стремятся__\n",
    ) {
        div {
            +"пристаням__стремятся"
            +"_"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample388() = doTest(
        markdown = "__foo, __bar__, baz__\n",
    ) {
        div {
            strong {
                +"foo,"
                +" "
                strong {
                    +"bar"
                }
                +","
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample389() = doTest(
        markdown = "foo-__(bar)__\n",
    ) {
        div {
            +"foo-"
            strong {
                +"("
                +"bar"
                +")"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample390() = doTest(
        markdown = "**foo bar **\n",
    ) {
        div {
            +"*"
            +"*"
            +"foo bar"
            +" "
            +"*"
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample391() = doTest(
        markdown = "**(**foo)\n",
    ) {
        div {
            +"*"
            +"*"
            +"("
            +"*"
            +"*"
            +"foo"
            +")"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample392() = doTest(
        markdown = "*(**foo**)*\n",
    ) {
        div {
            em {
                +"("
                strong {
                    +"foo"
                }
                +")"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample393() = doTest(
        markdown = "**Gomphocarpus (*Gomphocarpus physocarpus*, syn.\n*Asclepias physocarpa*)**\n",
    ) {
        div {
            strong {
                +"Gomphocarpus"
                +" "
                +"("
                em {
                    +"Gomphocarpus physocarpus"
                }
                +","
                +" "
                +"syn."
                +"\n"
                br()
                em {
                    +"Asclepias physocarpa"
                }
                +")"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample394() = doTest(
        markdown = "**foo \"*bar*\" foo**\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                +"\""
                em {
                    +"bar"
                }
                +"\""
                +" "
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample395() = doTest(
        markdown = "**foo**bar\n",
    ) {
        div {
            strong {
                +"foo"
            }
            +"bar"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample396() = doTest(
        markdown = "__foo bar __\n",
    ) {
        div {
            +"_"
            +"_"
            +"foo bar"
            +" "
            +"_"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample397() = doTest(
        markdown = "__(__foo)\n",
    ) {
        div {
            +"_"
            +"_"
            +"("
            +"_"
            +"_"
            +"foo"
            +")"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample398() = doTest(
        markdown = "_(__foo__)_\n",
    ) {
        div {
            em {
                +"("
                strong {
                    +"foo"
                }
                +")"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample399() = doTest(
        markdown = "__foo__bar\n",
    ) {
        div {
            +"_"
            +"_"
            +"foo__bar"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample400() = doTest(
        markdown = "__пристаням__стремятся\n",
    ) {
        div {
            +"_"
            +"_"
            +"пристаням__стремятся"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample401() = doTest(
        markdown = "__foo__bar__baz__\n",
    ) {
        div {
            strong {
                +"foo__bar__baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample402() = doTest(
        markdown = "__(bar)__.\n",
    ) {
        div {
            strong {
                +"("
                +"bar"
                +")"
            }
            +"."
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample403() = doTest(
        markdown = "*foo [bar](/url)*\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                a {
                    href = "/url"
                    +"bar"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample404() = doTest(
        markdown = "*foo\nbar*\n",
    ) {
        div {
            em {
                +"foo"
                +"\n"
                br()
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample405() = doTest(
        markdown = "_foo __bar__ baz_\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                strong {
                    +"bar"
                }
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample406() = doTest(
        markdown = "_foo _bar_ baz_\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                em {
                    +"bar"
                }
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample407() = doTest(
        markdown = "__foo_ bar_\n",
    ) {
        div {
            em {
                em {
                    +"foo"
                }
                +" "
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample408() = doTest(
        markdown = "*foo *bar**\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                em {
                    +"bar"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample409() = doTest(
        markdown = "*foo **bar** baz*\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                strong {
                    +"bar"
                }
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample410() = doTest(
        markdown = "*foo**bar**baz*\n",
    ) {
        div {
            em {
                +"foo"
                strong {
                    +"bar"
                }
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample411() = doTest(
        markdown = "*foo**bar*\n",
    ) {
        div {
            em {
                +"foo"
                +"*"
                +"*"
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample412() = doTest(
        markdown = "***foo** bar*\n",
    ) {
        div {
            em {
                strong {
                    +"foo"
                }
                +" "
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample413() = doTest(
        markdown = "*foo **bar***\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                strong {
                    +"bar"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample414() = doTest(
        markdown = "*foo**bar***\n",
    ) {
        div {
            em {
                +"foo"
                strong {
                    +"bar"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample415() = doTest(
        markdown = "foo***bar***baz\n",
    ) {
        div {
            +"foo"
            em {
                strong {
                    +"bar"
                }
            }
            +"baz"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample416() = doTest(
        markdown = "foo******bar*********baz\n",
    ) {
        div {
            +"foo"
            strong {
                strong {
                    strong {
                        +"bar"
                    }
                }
            }
            +"*"
            +"*"
            +"*"
            +"baz"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample417() = doTest(
        markdown = "*foo **bar *baz* bim** bop*\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                strong {
                    +"bar"
                    +" "
                    em {
                        +"baz"
                    }
                    +" "
                    +"bim"
                }
                +" "
                +"bop"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample418() = doTest(
        markdown = "*foo [*bar*](/url)*\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                a {
                    href = "/url"
                    em {
                        +"bar"
                    }
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample419() = doTest(
        markdown = "** is not an empty emphasis\n",
    ) {
        div {
            +"*"
            +"*"
            +" "
            +"is not an empty emphasis"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample420() = doTest(
        markdown = "**** is not an empty strong emphasis\n",
    ) {
        div {
            +"*"
            +"*"
            +"*"
            +"*"
            +" "
            +"is not an empty strong emphasis"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample421() = doTest(
        markdown = "**foo [bar](/url)**\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                a {
                    href = "/url"
                    +"bar"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample422() = doTest(
        markdown = "**foo\nbar**\n",
    ) {
        div {
            strong {
                +"foo"
                +"\n"
                br()
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample423() = doTest(
        markdown = "__foo _bar_ baz__\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                em {
                    +"bar"
                }
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample424() = doTest(
        markdown = "__foo __bar__ baz__\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                strong {
                    +"bar"
                }
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample425() = doTest(
        markdown = "____foo__ bar__\n",
    ) {
        div {
            strong {
                strong {
                    +"foo"
                }
                +" "
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample426() = doTest(
        markdown = "**foo **bar****\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                strong {
                    +"bar"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample427() = doTest(
        markdown = "**foo *bar* baz**\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                em {
                    +"bar"
                }
                +" "
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample428() = doTest(
        markdown = "**foo*bar*baz**\n",
    ) {
        div {
            strong {
                +"foo"
                em {
                    +"bar"
                }
                +"baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample429() = doTest(
        markdown = "***foo* bar**\n",
    ) {
        div {
            strong {
                em {
                    +"foo"
                }
                +" "
                +"bar"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample430() = doTest(
        markdown = "**foo *bar***\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                em {
                    +"bar"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testEmphasisAndStrongEmphasisExample431() = doTest(
        markdown = "**foo *bar **baz**\nbim* bop**\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                em {
                    +"bar"
                    +" "
                    strong {
                        +"baz"
                    }
                    +"\n"
                    br()
                    +"bim"
                }
                +" "
                +"bop"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample432() = doTest(
        markdown = "**foo [*bar*](/url)**\n",
    ) {
        div {
            strong {
                +"foo"
                +" "
                a {
                    href = "/url"
                    em {
                        +"bar"
                    }
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample433() = doTest(
        markdown = "__ is not an empty emphasis\n",
    ) {
        div {
            +"_"
            +"_"
            +" "
            +"is not an empty emphasis"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample434() = doTest(
        markdown = "____ is not an empty strong emphasis\n",
    ) {
        div {
            +"_"
            +"_"
            +"_"
            +"_"
            +" "
            +"is not an empty strong emphasis"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample435() = doTest(
        markdown = "foo ***\n",
    ) {
        div {
            +"foo"
            +" "
            +"*"
            +"*"
            +"*"
        }
    }

    @Test
    @Ignore
    fun testEmphasisAndStrongEmphasisExample436() = doTest(
        markdown = "foo *\\**\n",
    ) {
        div {
            +"foo"
            +" "
            em {
                +"*"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample437() = doTest(
        markdown = "foo *_*\n",
    ) {
        div {
            +"foo"
            +" "
            em {
                +"_"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample438() = doTest(
        markdown = "foo *****\n",
    ) {
        div {
            +"foo"
            +" "
            +"*"
            +"*"
            +"*"
            +"*"
            +"*"
        }
    }

    @Test
    @Ignore
    fun testEmphasisAndStrongEmphasisExample439() = doTest(
        markdown = "foo **\\***\n",
    ) {
        div {
            +"foo"
            +" "
            strong {
                +"*"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample440() = doTest(
        markdown = "foo **_**\n",
    ) {
        div {
            +"foo"
            +" "
            strong {
                +"_"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample441() = doTest(
        markdown = "**foo*\n",
    ) {
        div {
            +"*"
            em {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample442() = doTest(
        markdown = "*foo**\n",
    ) {
        div {
            em {
                +"foo"
            }
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample443() = doTest(
        markdown = "***foo**\n",
    ) {
        div {
            +"*"
            strong {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample444() = doTest(
        markdown = "****foo*\n",
    ) {
        div {
            +"*"
            +"*"
            +"*"
            em {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample445() = doTest(
        markdown = "**foo***\n",
    ) {
        div {
            strong {
                +"foo"
            }
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample446() = doTest(
        markdown = "*foo****\n",
    ) {
        div {
            em {
                +"foo"
            }
            +"*"
            +"*"
            +"*"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample447() = doTest(
        markdown = "foo ___\n",
    ) {
        div {
            +"foo"
            +" "
            +"_"
            +"_"
            +"_"
        }
    }

    @Test
    @Ignore
    fun testEmphasisAndStrongEmphasisExample448() = doTest(
        markdown = "foo _\\__\n",
    ) {
        div {
            +"foo"
            +" "
            em {
                +"_"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample449() = doTest(
        markdown = "foo _*_\n",
    ) {
        div {
            +"foo"
            +" "
            em {
                +"*"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample450() = doTest(
        markdown = "foo _____\n",
    ) {
        div {
            +"foo"
            +" "
            +"_"
            +"_"
            +"_"
            +"_"
            +"_"
        }
    }

    @Test
    @Ignore
    fun testEmphasisAndStrongEmphasisExample451() = doTest(
        markdown = "foo __\\___\n",
    ) {
        div {
            +"foo"
            +" "
            strong {
                +"_"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample452() = doTest(
        markdown = "foo __*__\n",
    ) {
        div {
            +"foo"
            +" "
            strong {
                +"*"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample453() = doTest(
        markdown = "__foo_\n",
    ) {
        div {
            +"_"
            em {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample454() = doTest(
        markdown = "_foo__\n",
    ) {
        div {
            em {
                +"foo"
            }
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample455() = doTest(
        markdown = "___foo__\n",
    ) {
        div {
            +"_"
            strong {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample456() = doTest(
        markdown = "____foo_\n",
    ) {
        div {
            +"_"
            +"_"
            +"_"
            em {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample457() = doTest(
        markdown = "__foo___\n",
    ) {
        div {
            strong {
                +"foo"
            }
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample458() = doTest(
        markdown = "_foo____\n",
    ) {
        div {
            em {
                +"foo"
            }
            +"_"
            +"_"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample459() = doTest(
        markdown = "**foo**\n",
    ) {
        div {
            strong {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample460() = doTest(
        markdown = "*_foo_*\n",
    ) {
        div {
            em {
                em {
                    +"foo"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample461() = doTest(
        markdown = "__foo__\n",
    ) {
        div {
            strong {
                +"foo"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample462() = doTest(
        markdown = "_*foo*_\n",
    ) {
        div {
            em {
                em {
                    +"foo"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample463() = doTest(
        markdown = "****foo****\n",
    ) {
        div {
            strong {
                strong {
                    +"foo"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample464() = doTest(
        markdown = "____foo____\n",
    ) {
        div {
            strong {
                strong {
                    +"foo"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample465() = doTest(
        markdown = "******foo******\n",
    ) {
        div {
            strong {
                strong {
                    strong {
                        +"foo"
                    }
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample466() = doTest(
        markdown = "***foo***\n",
    ) {
        div {
            em {
                strong {
                    +"foo"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample467() = doTest(
        markdown = "_____foo_____\n",
    ) {
        div {
            em {
                strong {
                    strong {
                        +"foo"
                    }
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample468() = doTest(
        markdown = "*foo _bar* baz_\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                +"_"
                +"bar"
            }
            +" "
            +"baz"
            +"_"
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample469() = doTest(
        markdown = "*foo __bar *baz bim__ bam*\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                strong {
                    +"bar"
                    +" "
                    +"*"
                    +"baz bim"
                }
                +" "
                +"bam"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample470() = doTest(
        markdown = "**foo **bar baz**\n",
    ) {
        div {
            +"*"
            +"*"
            +"foo"
            +" "
            strong {
                +"bar baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample471() = doTest(
        markdown = "*foo *bar baz*\n",
    ) {
        div {
            +"*"
            +"foo"
            +" "
            em {
                +"bar baz"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample472() = doTest(
        markdown = "*[bar*](/url)\n",
    ) {
        div {
            +"*"
            a {
                href = "/url"
                +"bar"
                +"*"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample473() = doTest(
        markdown = "_foo [bar_](/url)\n",
    ) {
        div {
            +"_"
            +"foo"
            +" "
            a {
                href = "/url"
                +"bar"
                +"_"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample474() = doTest(
        markdown = "*<img src=\"foo\" title=\"*\"/>\n",
    ) {
        div {
            +"*"
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<img src=\"foo\" title=\"*\"/>"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample475() = doTest(
        markdown = "**<a href=\"**\">\n",
    ) {
        div {
            +"*"
            +"*"
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"**\">"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample476() = doTest(
        markdown = "__<a href=\"__\">\n",
    ) {
        div {
            +"_"
            +"_"
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"__\">"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample477() = doTest(
        markdown = "*a `*`*\n",
    ) {
        div {
            em {
                +"a"
                +" "
                code {
                    +"*"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample478() = doTest(
        markdown = "_a `_`_\n",
    ) {
        div {
            em {
                +"a"
                +" "
                code {
                    +"_"
                }
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample479() = doTest(
        markdown = "**a<http://foo.bar/?q=**>\n",
    ) {
        div {
            +"*"
            +"*"
            +"a"
            a {
                href = "http://foo.bar/?q=**"
                +"http://foo.bar/?q=**"
            }
        }
    }

    @Test
    fun testEmphasisAndStrongEmphasisExample480() = doTest(
        markdown = "__a<http://foo.bar/?q=__>\n",
    ) {
        div {
            +"_"
            +"_"
            +"a"
            a {
                href = "http://foo.bar/?q=__"
                +"http://foo.bar/?q=__"
            }
        }
    }

    @Test
    fun testLinksExample481() = doTest(
        markdown = "[link](/uri \"title\")\n",
    ) {
        div {
            a {
                href = "/uri"
                title = "title"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample482() = doTest(
        markdown = "[link](/uri)\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample483() = doTest(
        markdown = "[](./target.md)\n",
    ) {
        div {
            a {
                href = "./target.md"
            }
        }
    }

    @Test
    fun testLinksExample484() = doTest(
        markdown = "[link]()\n",
    ) {
        div {
            a {
                href = ""
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample485() = doTest(
        markdown = "[link](<>)\n",
    ) {
        div {
            a {
                href = ""
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample486() = doTest(
        markdown = "[]()\n",
    ) {
        div {
            a {
                href = ""
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample487() = doTest(
        markdown = "[link](/my uri)\n",
    ) {
        div {
            +"[link](/my uri)"
        }
    }

    @Test
    fun testLinksExample488() = doTest(
        markdown = "[link](</my uri>)\n",
    ) {
        div {
            a {
                href = "/my%20uri"
                +"link"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testLinksExample489() = doTest(
        markdown = "[link](foo\nbar)\n",
    ) {
        div {
            +"["
            +"link"
            +"]"
            +"("
            +"foo"
            +"\n"
            br()
            +"bar"
            +")"
        }
    }

    @Test
    @Ignore
    fun testLinksExample490() = doTest(
        markdown = "[link](<foo\nbar>)\n",
    ) {
        div {
            +"[link](<foo\nbar>)"
        }
    }

    @Test
    fun testLinksExample491() = doTest(
        markdown = "[a](<b)c>)\n",
    ) {
        div {
            a {
                href = "b)c"
                +"a"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample492() = doTest(
        markdown = "[link](<foo\\>)\n",
    ) {
        div {
            +"["
            +"link"
            +"]"
            +"("
            +"<"
            +"foo\\>"
            +")"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testLinksExample493() = doTest(
        markdown = "[a](<b)c\n[a](<b)c>\n[a](<b>c)\n",
    ) {
        div {
            +"[a](<b)c\n[a](<b)c>\n[a](<b>c)"
        }
    }

    @Test
    fun testLinksExample494() = doTest(
        markdown = "[link](\\(foo\\))\n",
    ) {
        div {
            a {
                href = "(foo)"
                +"link"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample495() = doTest(
        markdown = "[link](foo(and(bar)))\n",
    ) {
        div {
            a {
                href = "foo(and(bar))"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample496() = doTest(
        markdown = "[link](foo(and(bar))\n",
    ) {
        div {
            +"["
            +"link"
            +"]"
            +"("
            +"foo"
            +"("
            +"and"
            +"("
            +"bar"
            +")"
            +")"
        }
    }

    @Test
    fun testLinksExample497() = doTest(
        markdown = "[link](foo\\(and\\(bar\\))\n",
    ) {
        div {
            a {
                href = "foo(and(bar)"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample498() = doTest(
        markdown = "[link](<foo(and(bar)>)\n",
    ) {
        div {
            a {
                href = "foo(and(bar)"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample499() = doTest(
        markdown = "[link](foo\\)\\:)\n",
    ) {
        div {
            a {
                href = "foo):"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample500() = doTest(
        markdown = "[link](#fragment)\n\n[link](http://example.com#fragment)\n\n[link](http://example.com?foo=3#frag)\n",
    ) {
        div {
            a {
                href = "#fragment"
                +"link"
            }
        }
        div {
            a {
                href = "http://example.com#fragment"
                +"link"
            }
        }
        div {
            a {
                href = "http://example.com?foo=3#frag"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample501() = doTest(
        markdown = "[link](foo\\bar)\n",
    ) {
        div {
            a {
                href = "foo%5Cbar"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample502() = doTest(
        markdown = "[link](foo%20b&auml;)\n",
    ) {
        div {
            a {
                href = "foo%20b%C3%A4"
                +"link"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample503() = doTest(
        markdown = "[link](\"title\")\n",
    ) {
        div {
            a {
                href = "%22title%22"
                +"link"
            }
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testLinksExample504() = doTest(
        markdown = "[link](/url \"title\")\n[link](/url 'title')\n[link](/url (title))\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"link"
            }
            +"\n"
            br()
            a {
                href = "/url"
                title = "title"
                +"link"
            }
            +"\n"
            br()
            a {
                href = "/url"
                title = "title"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample505() = doTest(
        markdown = "[link](/url \"title \\\"&quot;\")\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title &quot;&quot;"
                +"link"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample506() = doTest(
        markdown = "[link](/url \"title\")\n",
    ) {
        div {
            a {
                href = "/url%C2%A0%22title%22"
                +"link"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample507() = doTest(
        markdown = "[link](/url \"title \"and\" title\")\n",
    ) {
        div {
            +"["
            +"link"
            +"]"
            +"("
            +"/url"
            +" "
            +"\""
            +"title"
            +" "
            +"\""
            +"and"
            +"\""
            +" "
            +"title"
            +"\""
            +")"
        }
    }

    @Test
    fun testLinksExample508() = doTest(
        markdown = "[link](/url 'title \"and\" title')\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title &quot;and&quot; title"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample509() = doTest(
        markdown = "[link](   /uri\n  \"title\"  )\n",
    ) {
        div {
            a {
                href = "/uri"
                title = "title"
                +"link"
            }
        }
    }

    @Test
    fun testLinksExample510() = doTest(
        markdown = "[link] (/uri)\n",
    ) {
        div {
            +"["
            +"link"
            +"]"
            +" "
            +"("
            +"/uri"
            +")"
        }
    }

    @Test
    fun testLinksExample511() = doTest(
        markdown = "[link [foo [bar]]](/uri)\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
                +" "
                +"["
                +"foo"
                +" "
                +"["
                +"bar"
                +"]"
                +"]"
            }
        }
    }

    @Test
    fun testLinksExample512() = doTest(
        markdown = "[link] bar](/uri)\n",
    ) {
        div {
            +"["
            +"link"
            +"]"
            +" "
            +"bar"
            +"]"
            +"("
            +"/uri"
            +")"
        }
    }

    @Test
    fun testLinksExample513() = doTest(
        markdown = "[link [bar](/uri)\n",
    ) {
        div {
            +"["
            +"link"
            +" "
            a {
                href = "/uri"
                +"bar"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample514() = doTest(
        markdown = "[link \\[bar](/uri)\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
                +" "
                +"\\[bar"
            }
        }
    }

    @Test
    fun testLinksExample515() = doTest(
        markdown = "[link *foo **bar** `#`*](/uri)\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
                +" "
                em {
                    +"foo"
                    +" "
                    strong {
                        +"bar"
                    }
                    +" "
                    code {
                        +"#"
                    }
                }
            }
        }
    }

    @Test
    fun testLinksExample516() = doTest(
        markdown = "[![moon](moon.jpg)](/uri)\n",
    ) {
        div {
            a {
                href = "/uri"
                img {
                    src = "moon.jpg"
                    alt = "moon"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample517() = doTest(
        markdown = "[foo [bar](/uri)](/uri)\n",
    ) {
        div {
            +"["
            +"foo"
            +" "
            a {
                href = "/uri"
                +"bar"
            }

            +"]"
            +"("
            +"/uri"
            +")"
        }
    }

    @Test
    @Ignore
    fun testLinksExample518() = doTest(
        markdown = "[foo *[bar [baz](/uri)](/uri)*](/uri)\n",
    ) {
        div {
            +"["
            +"foo"
            +" "
            em {
                +"["
                +"bar"
                +" "
                a {
                    href = "/uri"
                    +"baz"
                }
                +"]"
                +"("
                +"/uri"
                +")"
            }
            +"]"
            +"("
            +"/uri"
            +")"
        }
    }

    @Test
    @Ignore
    fun testLinksExample519() = doTest(
        markdown = "![[[foo](uri1)](uri2)](uri3)\n",
    ) {
        div {
            img {
                src = "uri3"
                alt = "[foo](uri2)"
            }
        }
    }

    @Test
    fun testLinksExample520() = doTest(
        markdown = "*[foo*](/uri)\n",
    ) {
        div {
            +"*"
            a {
                href = "/uri"
                +"foo"
                +"*"
            }
        }
    }

    @Test
    fun testLinksExample521() = doTest(
        markdown = "[foo *bar](baz*)\n",
    ) {
        div {
            a {
                href = "baz*"
                +"foo"
                +" "
                +"*"
                +"bar"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample522() = doTest(
        markdown = "*foo [bar* baz]\n",
    ) {
        div {
            em {
                +"foo"
                +" "
                +"["
                +"bar"
            }
            +" "
            +"baz"
            +"]"
        }
    }

    @Test
    fun testLinksExample523() = doTest(
        markdown = "[foo <bar attr=\"](baz)\">\n",
    ) {
        div {
            +"["
            +"foo"
            +" "
            +""
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<bar attr=\"](baz)\">"
                }
            }
        }
    }

    @Test
    fun testLinksExample524() = doTest(
        markdown = "[foo`](/uri)`\n",
    ) {
        div {
            +"["
            +"foo"
            code {
                +"](/uri)"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample525() = doTest(
        markdown = "[foo<http://example.com/?search=](uri)>\n",
    ) {
        div {
            +"["
            +"foo"
            a {
                href = "http://example.com/?search=%5D(uri)"
                +"http://example.com/?search=](uri)"
            }
        }
    }

    @Test
    fun testLinksExample526() = doTest(
        markdown = "[foo][bar]\n\n[bar]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    @Test
    fun testLinksExample527() = doTest(
        markdown = "[link [foo [bar]]][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
                +" "
                +"["
                +"foo"
                +" "
                +"["
                +"bar"
                +"]"
                +"]"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample528() = doTest(
        markdown = "[link \\[bar][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
                +" "
                +"\\[bar"
            }
        }
    }

    @Test
    fun testLinksExample529() = doTest(
        markdown = "[link *foo **bar** `#`*][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            a {
                href = "/uri"
                +"link"
                +" "
                em {
                    +"foo"
                    +" "
                    strong {
                        +"bar"
                    }
                    +" "
                    code {
                        +"#"
                    }
                }
            }
        }
    }

    @Test
    fun testLinksExample530() = doTest(
        markdown = "[![moon](moon.jpg)][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            a {
                href = "/uri"
                img {
                    src = "moon.jpg"
                    alt = "moon"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample531() = doTest(
        markdown = "[foo [bar](/uri)][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            +" "
            a {
                href = "/uri"
                +"bar"
            }
            +"]"
            a {
                href = "/uri"
                +"ref"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample532() = doTest(
        markdown = "[foo *bar [baz][ref]*][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            +" "
            em {
                +"bar"
                +" "
                a {
                    href = "/uri"
                    +"baz"
                }
            }
            +"]"
            a {
                href = "/uri"
                +"ref"
            }
        }
    }

    @Test
    fun testLinksExample533() = doTest(
        markdown = "*[foo*][ref]\n\n[ref]: /uri\n",
    ) {
        div {
            +"*"
            a {
                href = "/uri"
                +"foo"
                +"*"
            }
        }
    }

    @Test
    fun testLinksExample534() = doTest(
        markdown = "[foo *bar][ref]*\n\n[ref]: /uri\n",
    ) {
        div {
            a {
                href = "/uri"
                +"foo"
                +" "
                +"*"
                +"bar"
            }
            +"*"
        }
    }

    @Test
    fun testLinksExample535() = doTest(
        markdown = "[foo <bar attr=\"][ref]\">\n\n[ref]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<bar attr=\"][ref]\">"
                }
            }
        }
    }

    @Test
    fun testLinksExample536() = doTest(
        markdown = "[foo`][ref]`\n\n[ref]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            code {
                +"][ref]"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample537() = doTest(
        markdown = "[foo<http://example.com/?search=][ref]>\n\n[ref]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            a {
                href = "http://example.com/?search=%5D%5Bref%5D"
                +"http://example.com/?search=][ref]"
            }
        }
    }

    @Test
    fun testLinksExample538() = doTest(
        markdown = "[foo][BaR]\n\n[bar]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample539() = doTest(
        markdown = "[ẞ]\n\n[SS]: /url\n",
    ) {
        div {
            a {
                href = "/uri"
                +"ẞ"
            }
        }
    }

    @Test
    fun testLinksExample540() = doTest(
        markdown = "[Foo\n  bar]: /url\n\n[Baz][Foo bar]\n",
    ) {
        div {
            a {
                href = "/url"
                +"Baz"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample541() = doTest(
        markdown = "[foo] [bar]\n\n[bar]: /url \"title\"\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            a {
                href = "/uri"
                title = "title"
                +"bar"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample542() = doTest(
        markdown = "[foo]\n[bar]\n\n[bar]: /url \"title\"\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            +"\n"
            a {
                href = "/uri"
                title = "title"
                +"bar"
            }
        }
    }

    @Test
    fun testLinksExample543() = doTest(
        markdown = "[foo]: /url1\n\n[foo]: /url2\n\n[bar][foo]\n",
    ) {
        div {
            a {
                href = "/url1"
                +"bar"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample544() = doTest(
        markdown = "[bar][foo\\!]\n\n[foo!]: /url\n",
    ) {
        div {
            +"["
            +"bar"
            +"]"
            +"["
            +"foo\\!"
            +"]"
        }
    }

    @Test
    fun testLinksExample545() = doTest(
        markdown = "[foo][ref[]\n\n[ref[]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            +"["
            +"ref"
            +"["
            +"]"
        }
        div {
            +"["
            +"ref"
            +"["
            +"]"
            +":"
            +" "
            +"/uri"
        }
    }

    @Test
    fun testLinksExample546() = doTest(
        markdown = "[foo][ref[bar]]\n\n[ref[bar]]: /uri\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            +"["
            +"ref"
            +"["
            +"bar"
            +"]"
            +"]"
        }
        div {
            +"["
            +"ref"
            +"["
            +"bar"
            +"]"
            +"]"
            +":"
            +" "
            +"/uri"
        }
    }

    @Test
    fun testLinksExample547() = doTest(
        markdown = "[[[foo]]]\n\n[[[foo]]]: /url\n",
    ) {
        div {
            +"["
            +"["
            +"["
            +"foo"
            +"]"
            +"]"
            +"]"
        }
        div {
            +"["
            +"["
            +"["
            +"foo"
            +"]"
            +"]"
            +"]"
            +":"
            +" "
            +"/url"
        }
    }

    @Test
    fun testLinksExample548() = doTest(
        markdown = "[foo][ref\\[]\n\n[ref\\[]: /uri\n",
    ) {
        div {
            a {
                href = "/uri"
                +"foo"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample549() = doTest(
        markdown = "[bar\\\\]: /uri\n\n[bar\\\\]\n",
    ) {
        div {
            a {
                href = "/uri"
                +"bar\\\\"
            }
        }
    }

    @Test
    fun testLinksExample550() = doTest(
        markdown = "[]\n\n[]: /uri\n",
    ) {
        div {
            +"["
            +"]"
        }
        div {
            +"["
            +"]"
            +":"
            +" "
            +"/uri"
        }
    }

    @Test
    @Ignore
    fun testLinksExample551() = doTest(
        markdown = "[\n ]\n\n[\n ]: /uri\n",
    ) {
        div {
            +"["
            +"\n"
            +"]"
        }
        div {
            +"["
            +"\n"
            +"]"
            +":"
            +" "
            +"/uri"
        }
    }

    @Test
    fun testLinksExample552() = doTest(
        markdown = "[foo][]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    @Test
    fun testLinksExample553() = doTest(
        markdown = "[*foo* bar][]\n\n[*foo* bar]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                em {
                    +"foo"
                }
                +" "
                +"bar"
            }
        }
    }

    @Test
    fun testLinksExample554() = doTest(
        markdown = "[Foo][]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"Foo"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample555() = doTest(
        markdown = "[foo] \n[]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
            +"["
            +"]"
        }
    }

    @Test
    fun testLinksExample556() = doTest(
        markdown = "[foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    @Test
    fun testLinksExample557() = doTest(
        markdown = "[*foo* bar]\n\n[*foo* bar]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                em {
                    +"foo"
                }
                +" "
                +"bar"
            }
        }
    }

    @Test
    fun testLinksExample558() = doTest(
        markdown = "[[*foo* bar]]\n\n[*foo* bar]: /url \"title\"\n",
    ) {
        div {
            +"["
            a {
                href = "/url"
                title = "title"
                em {
                    +"foo"
                }
                +" "
                +"bar"
            }
            +"]"
        }
    }

    @Test
    fun testLinksExample559() = doTest(
        markdown = "[[bar [foo]\n\n[foo]: /url\n",
    ) {
        div {
            +"["
            +"["
            +"bar"
            +" "
            a {
                href = "/url"
                +"foo"
            }
        }
    }

    @Test
    fun testLinksExample560() = doTest(
        markdown = "[Foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            a {
                href = "/url"
                title = "title"
                +"Foo"
            }
        }
    }

    @Test
    fun testLinksExample561() = doTest(
        markdown = "[foo] bar\n\n[foo]: /url\n",
    ) {
        div {
            a {
                href = "/url"
                +"foo"
            }
            +" "
            +"bar"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testLinksExample562() = doTest(
        markdown = "\\[foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            +"\\[foo"
            +"]"
        }
    }

    @Test
    fun testLinksExample563() = doTest(
        markdown = "[foo*]: /url\n\n*[foo*]\n",
    ) {
        div {
            +"*"
            a {
                href = "/url"
                +"foo"
                +"*"
            }
        }
    }

    @Test
    fun testLinksExample564() = doTest(
        markdown = "[foo][bar]\n\n[foo]: /url1\n[bar]: /url2\n",
    ) {
        div {
            a {
                href = "/url2"
                +"foo"
            }
        }
    }

    @Test
    fun testLinksExample565() = doTest(
        markdown = "[foo][]\n\n[foo]: /url1\n",
    ) {
        div {
            a {
                href = "/url1"
                +"foo"
            }
        }
    }

    @Test
    fun testLinksExample566() = doTest(
        markdown = "[foo]()\n\n[foo]: /url1\n",
    ) {
        div {
            a {
                href = ""
                +"foo"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample567() = doTest(
        markdown = "[foo](not a link)\n\n[foo]: /url1\n",
    ) {
        div {
            a {
                href = "/url1"
                +"foo"
            }
            +"("
            +"not a link"
            +")"
        }
    }

    @Test
    @Ignore
    fun testLinksExample568() = doTest(
        markdown = "[foo][bar][baz]\n\n[baz]: /url\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            a {
                href = "/url"
                +"bar"
            }
        }
    }

    @Test
    fun testLinksExample569() = doTest(
        markdown = "[foo][bar][baz]\n\n[baz]: /url1\n[bar]: /url2\n",
    ) {
        div {
            a {
                href = "/url2"
                +"foo"
            }
            a {
                href = "/url1"
                +"baz"
            }
        }
    }

    @Test
    @Ignore
    fun testLinksExample570() = doTest(
        markdown = "[foo][bar][baz]\n\n[baz]: /url1\n[foo]: /url2\n",
    ) {
        div {
            +"["
            +"foo"
            +"]"
            a {
                href = "/url1"
                +"bar"
            }
        }
    }

    @Test
    fun testImagesExample571() = doTest(
        markdown = "![foo](/url \"title\")\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo"
                title = "title"
            }
        }
    }

    @Test
    fun testImagesExample572() = doTest(
        markdown = "![foo *bar*]\n\n[foo *bar*]: train.jpg \"train & tracks\"\n",
    ) {
        div {
            img {
                src = "train.jpg"
                alt = "foo bar"
                title = "train &amp; tracks"
            }
        }
    }

    @Test
    @Ignore
    fun testImagesExample573() = doTest(
        markdown = "![foo ![bar](/url)](/url2)\n",
    ) {
        div {
            img {
                src = "/url2"
                alt = "foo bar"
            }
        }
    }

    @Test
    @Ignore
    fun testImagesExample574() = doTest(
        markdown = "![foo [bar](/url)](/url2)\n",
    ) {
        div {
            img {
                src = "/url2"
                alt = "foo bar"
            }
        }
    }

    @Test
    fun testImagesExample575() = doTest(
        markdown = "![foo *bar*][]\n\n[foo *bar*]: train.jpg \"train & tracks\"\n",
    ) {
        div {
            img {
                src = "train.jpg"
                alt = "foo bar"
                title = "train &amp; tracks"
            }
        }
    }

    @Test
    fun testImagesExample576() = doTest(
        markdown = "![foo *bar*][foobar]\n\n[FOOBAR]: train.jpg \"train & tracks\"\n",
    ) {
        div {
            img {
                src = "train.jpg"
                alt = "foo bar"
                title = "train &amp; tracks"
            }
        }
    }

    @Test
    fun testImagesExample577() = doTest(
        markdown = "![foo](train.jpg)\n",
    ) {
        div {
            img {
                src = "train.jpg"
                alt = "foo"
            }
        }
    }

    @Test
    fun testImagesExample578() = doTest(
        markdown = "My ![foo bar](/path/to/train.jpg  \"title\"   )\n",
    ) {
        div {
            +"My"
            +" "
            img {
                src = "/path/to/train.jpg"
                alt = "foo bar"
                title = "title"
            }
        }
    }

    @Test
    fun testImagesExample579() = doTest(
        markdown = "![foo](<url>)\n",
    ) {
        div {
            img {
                src = "url"
                alt = "foo"
            }
        }
    }

    @Test
    fun testImagesExample580() = doTest(
        markdown = "![](/url)\n",
    ) {
        div {
            img {
                src = "/url"
                alt = ""
            }
        }
    }

    @Test
    fun testImagesExample581() = doTest(
        markdown = "![foo][bar]\n\n[bar]: /url\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo"
            }
        }
    }

    @Test
    fun testImagesExample582() = doTest(
        markdown = "![foo][bar]\n\n[BAR]: /url\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo"
            }
        }
    }

    @Test
    fun testImagesExample583() = doTest(
        markdown = "![foo][]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo"
                title = "title"
            }
        }
    }

    @Test
    fun testImagesExample584() = doTest(
        markdown = "![*foo* bar][]\n\n[*foo* bar]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo bar"
                title = "title"
            }
        }
    }

    @Test
    fun testImagesExample585() = doTest(
        markdown = "![Foo][]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "Foo"
                title = "title"
            }
        }
    }

    @Test
    @Ignore
    fun testImagesExample586() = doTest(
        markdown = "![foo] \n[]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo"
                title = "titile"
            }
            +"[]"
        }
    }

    @Test
    fun testImagesExample587() = doTest(
        markdown = "![foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo"
                title = "title"
            }
        }
    }

    @Test
    fun testImagesExample588() = doTest(
        markdown = "![*foo* bar]\n\n[*foo* bar]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "foo bar"
                title = "title"
            }
        }
    }

    /**
     * Ignored
     * Reason: Duplicates with obsidian link style
     * Related: [TestObsidianElement.testObsidianLink1]
     *
     Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testImagesExample589() = doTest(
        markdown = "![[foo]]\n\n[[foo]]: /url \"title\"\n",
    ) {
        div {
            +"!"
            +"["
            +"["
            +"foo"
            +"]"
            +"]"
        }
        div {
            +"["
            +"["
            +"foo"
            +"]"
            +"]"
            +":"
            +" "
            +"/url"
            +" "
            +"\""
            +"title"
            +"\""
        }
    }

    @Test
    fun testImagesExample590() = doTest(
        markdown = "![Foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            img {
                src = "/url"
                alt = "Foo"
                title = "title"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testImagesExample591() = doTest(
        markdown = "!\\[foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            +"!"
            +"\\[foo"
            +"]"
        }
    }

    @Test
    fun testImagesExample592() = doTest(
        markdown = "\\![foo]\n\n[foo]: /url \"title\"\n",
    ) {
        div {
            +"\\!"
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    @Test
    fun testAutolinksExample593() = doTest(
        markdown = "<http://foo.bar.baz>\n",
    ) {
        div {
            a {
                href = "http://foo.bar.baz"
                +"http://foo.bar.baz"
            }
        }
    }

    @Test
    fun testAutolinksExample594() = doTest(
        markdown = "<http://foo.bar.baz/test?q=hello&id=22&boolean>\n",
    ) {
        div {
            a {
                href = "http://foo.bar.baz/test?q=hello&amp;id=22&amp;boolean"
                +"http://foo.bar.baz/test?q=hello&amp;id=22&amp;boolean"
            }
        }
    }

    @Test
    fun testAutolinksExample595() = doTest(
        markdown = "<irc://foo.bar:2233/baz>\n",
    ) {
        div {
            a {
                href = "irc://foo.bar:2233/baz"
                +"irc://foo.bar:2233/baz"
            }
        }
    }

    @Test
    fun testAutolinksExample596() = doTest(
        markdown = "<MAILTO:FOO@BAR.BAZ>\n",
    ) {
        div {
            a {
                href = "MAILTO:FOO@BAR.BAZ"
                +"MAILTO:FOO@BAR.BAZ"
            }
        }
    }

    @Test
    @Ignore
    fun testAutolinksExample597() = doTest(
        markdown = "<a+b+c:d>\n",
    ) {
        div {
            a {
                href = "a+b+c:d"
                +"a+b+c:d"
            }
        }
    }

    @Test
    @Ignore
    fun testAutolinksExample598() = doTest(
        markdown = "<made-up-scheme://foo,bar>\n",
    ) {
        div {
            a {
                href = "made-up-scheme://foo,bar"
                +"made-up-scheme://foo,bar"
            }
        }
    }

    @Test
    fun testAutolinksExample599() = doTest(
        markdown = "<http://../>\n",
    ) {
        div {
            a {
                href = "http://../"
                +"http://../"
            }
        }
    }

    @Test
    fun testAutolinksExample600() = doTest(
        markdown = "<localhost:5001/foo>\n",
    ) {
        div {
            a {
                href = "localhost:5001/foo"
                +"localhost:5001/foo"
            }
        }
    }

    @Test
    fun testAutolinksExample601() = doTest(
        markdown = "<http://foo.bar/baz bim>\n",
    ) {
        div {
            +"<"
            +"http"
            +":"
            +"//foo.bar/baz bim"
            +">"
        }
    }

    @Test
    @Ignore
    fun testAutolinksExample602() = doTest(
        markdown = "<http://example.com/\\[\\>\n",
    ) {
        div {
            a {
                href = "http://example.com/%5C%5B%5C"
                +"http://example.com/\\[\\"
            }
        }
    }

    @Test
    @Ignore
    fun testAutolinksExample603() = doTest(
        markdown = "<foo@bar.example.com>\n",
    ) {
        div {
            a {
                href = "mailto:foo@bar.example.com"
                +"foo@bar.example.com"
            }
        }
    }

    @Test
    @Ignore
    fun testAutolinksExample604() = doTest(
        markdown = "<foo+special@Bar.baz-bar0.com>\n",
    ) {
        div {
            a {
                href = "mailto:foo+special@Bar.baz-bar0.com"
                +"foo+special@Bar.baz-bar0.com"
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testAutolinksExample605() = doTest(
        markdown = "<foo\\+@bar.example.com>\n",
    ) {
        div {
            +"<"
            +"foo\\+@bar.example.com"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testAutolinksExample606() = doTest(
        markdown = "<>\n",
    ) {
        div {
            +"<"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testAutolinksExample607() = doTest(
        markdown = "< http://foo.bar >\n",
    ) {
        div {
            +"<"
            +" "
            +"http"
            +":"
            +"//foo.bar"
            +" "
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testAutolinksExample608() = doTest(
        markdown = "<m:abc>\n",
    ) {
        div {
            +"<"
            +"m"
            +":"
            +"abc"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testAutolinksExample609() = doTest(
        markdown = "<foo.bar.baz>\n",
    ) {
        div {
            +"<"
            +"foo.bar.baz"
            +">"
        }
    }

    @Test
    fun testAutolinksExample610() = doTest(
        markdown = "http://example.com\n",
    ) {
        div {
            +"http"
            +":"
            +"//example.com"
        }
    }

    @Test
    fun testAutolinksExample611() = doTest(
        markdown = "foo@bar.example.com\n",
    ) {
        div {
            +"foo@bar.example.com"
        }
    }

    @Test
    fun testRawHTMLExample612() = doTest(
        markdown = "<a><bab><c2c>\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a>"
                }
            }
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<bab>"
                }
            }
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<c2c>"
                }
            }
        }
    }

    @Test
    fun testRawHTMLExample613() = doTest(
        markdown = "<a/><b2/>\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a/>"
                }
            }
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<b2/>"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testRawHTMLExample614() = doTest(
        markdown = "<a  /><b2\ndata=\"foo\" >\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a />"
                }
            }
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<b2\ndata=\"foo\" >"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testRawHTMLExample615() = doTest(
        markdown = "<a foo=\"bar\" bam = 'baz <em>\"</em>'\n_boolean zoop:33=zoop:33 />\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a foo=\"bar\" bam = 'baz <em>\"</em>'\n_boolean zoop:33=zoop:33 />"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testRawHTMLExample616() = doTest(
        markdown = "Foo <responsive-image src=\"foo.jpg\" />\n",
    ) {
        div {
            +"Foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<responsive-image src=\"foo.jpg\" />"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample617() = doTest(
        markdown = "<33> <__>\n",
    ) {
        div {
            +"<"
            +"33"
            +">"
            +" "
            +"<"
            +"_"
            +"_"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample618() = doTest(
        markdown = "<a h*#ref=\"hi\">\n",
    ) {
        div {
            +"<"
            +"a h"
            +"*"
            +"#ref="
            +"\""
            +"hi"
            +"\""
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample619() = doTest(
        markdown = "<a href=\"hi'> <a href=hi'>\n",
    ) {
        div {
            +"<"
            +"a href="
            +"\""
            +"hi"
            +"'"
            +">"
            +" "
            +"<"
            +"a href=hi"
            +"'"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     *
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample620() = doTest(
        markdown = "< a><\nfoo><bar/ >\n<foo bar=baz\nbim!bop />\n",
    ) {
        div {
            +"<"
            +" "
            +"a"
            +">"
            +"<"
            +"\n"
            br()
            +"foo"
            +">"
            +"<"
            +"bar/"
            +" "
            +">"
            +"\n"
            br()
            +"<"
            +"foo bar=baz"
            +"\n"
            br()
            +"bim"
            +"!"
            +"bop"
            +" "
            +"/"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample621() = doTest(
        markdown = "<a href='bar'title=title>\n",
    ) {
        div {
            +"<"
            +"a href="
            +"'"
            +"bar"
            +"'"
            +"title=title"
            +">"
        }
    }

    @Test
    fun testRawHTMLExample622() = doTest(
        markdown = "</a></foo >\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "</a>"
                }
            }
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "</foo >"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample623() = doTest(
        markdown = "</a href=\"foo\">\n",
    ) {
        div {
            +"<"
            +"/a href="
            +"\""
            +"foo"
            +"\""
            +">"
        }
    }

    @Test
    fun testRawHTMLExample624() = doTest(
        markdown = "foo <!-- this is a\ncomment - with hyphen -->\n",
    ) {
        div {
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<!-- this is a\ncomment - with hyphen -->"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    fun testRawHTMLExample625() = doTest(
        markdown = "foo <!-- not a comment -- two hyphens -->\n",
    ) {
        div {
            +"foo"
            +" "
            +"<"
            +"!"
            +"--"
            +" "
            +"not a comment"
            +" "
            +"--"
            +" "
            +"two hyphens"
            +" "
            +"--"
            +">"
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testRawHTMLExample626() = doTest(
        markdown = "foo <!--> foo -->\n\nfoo <!-- foo--->\n",
    ) {
        div {
            +"foo"
            +" "
            +"<"
            +"!"
            +"--"
            +">"
            +" "
            +"foo"
            +" "
            +"--"
            +">"
        }
        div {
            +"foo"
            +" "
            +"<"
            +"!"
            +"--"
            +" "
            +"foo"
            +"---"
            +">"
        }
    }

    @Test
    fun testRawHTMLExample627() = doTest(
        markdown = "foo <?php echo \$a; ?>\n",
    ) {
        div {
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<?php echo \$a; ?>"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testRawHTMLExample628() = doTest(
        markdown = "foo <!ELEMENT br EMPTY>\n",
    ) {
        div {
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<!ELEMENT br EMPTY>"
                }
            }
        }
    }

    @Test
    fun testRawHTMLExample629() = doTest(
        markdown = "foo <![CDATA[>&<]]>\n",
    ) {
        div {
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<![CDATA[>&<]]>"
                }
            }
        }
    }

    @Test
    fun testRawHTMLExample630() = doTest(
        markdown = "foo <a href=\"&ouml;\">\n",
    ) {
        div {
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"&ouml;\">"
                }
            }
        }
    }

    @Test
    fun testRawHTMLExample631() = doTest(
        markdown = "foo <a href=\"\\*\">\n",
    ) {
        div {
            +"foo"
            +" "
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"\\*\">"
                }
            }
        }
    }

    /**
     * Modified
     * Reason: Disabled substitution of specific escape characters.
     */
    @Test
    @Ignore
    fun testRawHTMLExample632() = doTest(
        markdown = "<a href=\"\\\"\">\n",
    ) {
        div {
            +"<"
            +"a"
            +" "
            +"href="
            +"\""
            +"\""
            +"\""
            +">"
        }
    }

    @Test
    fun testHardLineBreaksExample633() = doTest(
        markdown = "foo  \nbaz\n",
    ) {
        div {
            +"foo"
            br()
            +"\n"
            +"baz"
        }
    }

    @Test
    fun testHardLineBreaksExample634() = doTest(
        markdown = "foo\\\nbaz\n",
    ) {
        div {
            +"foo"
            br()
            +"\n"
            +"baz"
        }
    }

    @Test
    fun testHardLineBreaksExample635() = doTest(
        markdown = "foo       \nbaz\n",
    ) {
        div {
            +"foo"
            br()
            +"\n"
            +"baz"
        }
    }

    @Test
    @Ignore
    fun testHardLineBreaksExample636() = doTest(
        markdown = "foo  \n     bar\n",
    ) {
        div {
            +"foo"
            br()
            +"bar"
        }
    }

    @Test
    @Ignore
    fun testHardLineBreaksExample637() = doTest(
        markdown = "foo\\\n     bar\n",
    ) {
        div {
            +"foo"
            br()
            +"\n"
            +"bar"
        }
    }

    @Test
    fun testHardLineBreaksExample638() = doTest(
        markdown = "*foo  \nbar*\n",
    ) {
        div {
            em {
                +"foo"
                br()
                +"\n"
                +"bar"
            }
        }
    }

    @Test
    fun testHardLineBreaksExample639() = doTest(
        markdown = "*foo\\\nbar*\n",
    ) {
        div {
            em {
                +"foo"
                br()
                +"\n"
                +"bar"
            }
        }
    }

    @Test
    @Ignore
    fun testHardLineBreaksExample640() = doTest(
        markdown = "`code  \nspan`\n",
    ) {
        div {
            code {
                +"code   span"
            }
        }
    }

    @Test
    @Ignore
    fun testHardLineBreaksExample641() = doTest(
        markdown = "`code\\\nspan`\n",
    ) {
        div {
            code {
                +"code\\ span"
            }
        }
    }

    @Test
    @Ignore
    fun testHardLineBreaksExample642() = doTest(
        markdown = "<a href=\"foo  \nbar\">\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"foo  \nbar\">"
                }
            }
        }
    }

    @Test
    @Ignore
    fun testHardLineBreaksExample643() = doTest(
        markdown = "<a href=\"foo\\\nbar\">\n",
    ) {
        div {
            div {
                dangerouslySetInnerHTML = jso {
                    __html = "<a href=\"foo\\\nbar\">"
                }
            }
        }
    }

    @Test
    fun testHardLineBreaksExample644() = doTest(
        markdown = "foo\\\n",
    ) {
        div {
            +"foo\\"
        }
    }

    @Test
    fun testHardLineBreaksExample645() = doTest(
        markdown = "foo  \n",
    ) {
        div {
            +"foo"
        }
    }

    @Test
    fun testHardLineBreaksExample646() = doTest(
        markdown = "### foo\\\n",
    ) {
        h3 {
            +"foo\\"
        }
    }

    @Test
    fun testHardLineBreaksExample647() = doTest(
        markdown = "### foo  \n",
    ) {
        h3 {
            +"foo"
        }
    }

    /**
     * Modified
     * Reason: Overlapping with obsidian line breaking rule
     * Related: [TestObsidianElement.testObsidianLineBreak1]
     */
    @Test
    fun testSoftLineBreaksExample648() = doTest(
        markdown = "foo\nbaz\n",
    ) {
        div {
            +"foo"
            +"\n"
            br()
            +"baz"
        }
    }

    @Test
    @Ignore
    fun testSoftLineBreaksExample649() = doTest(
        markdown = "foo \n baz\n",
    ) {
        div {
            +"foo"
            +"\n"
            +"baz"
        }
    }

    /**
     * Modified
     * Reason: To detect TeX code, duller char is treated as special MarkdownToken.
     * Related: [TestObsidianElement.testTeX1]
     */
    @Test
    fun testTextualContentExample650() = doTest(
        markdown = "hello \$.;'there\n",
    ) {
        div {
            +"hello"
            +" "
            +"\$"
            +".;"
            +"'"
            +"there"
        }
    }

    @Test
    fun testTextualContentExample651() = doTest(
        markdown = "Foo χρῆν\n",
    ) {
        div {
            +"Foo χρῆν"
        }
    }

    @Test
    fun testTextualContentExample652() = doTest(
        markdown = "Multiple     spaces\n",
    ) {
        div {
            +"Multiple     spaces"
        }
    }
}
