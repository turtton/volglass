package markdown

import csstype.ClassName
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.js.jso
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
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.pre
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.tr
import react.dom.html.ReactHTML.ul

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
                p {
                    +"foo"
                }
                p {
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
                p {
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

    @Test
    fun testBackslashEscapesExample12() = doTest("\\!\\\"\\#\\\$\\%\\&\\'\\(\\)\\*\\+\\,\\-\\.\\/\\:\\;\\<\\=\\>\\?\\@\\[\\\\\\]\\^\\_\\`\\{\\|\\}\\~\n") {
        p {
            +"!&quot;#\$%&amp;'()*+,-./:;&lt;=&gt;?@[\\]^_"
            +"`"
            +"{|}~"
        }
    }

    @Test
    fun testBackslashEscapesExample13() = doTest("\\\t\\A\\a\\ \\3\\φ\\«\n") {
        p {
            +"\\"
            +"\t"
            +"\\A\\a\\"
            +" "
            +"\\3\\φ\\«"
        }
    }

    @Test
    fun testBackslashEscapesExample14() = doTest("\\*not emphasized*\n\\<br/> not a tag\n\\[not a link](/foo)\n\\`not code`\n1\\. not a list\n\\* not a list\n\\# not a heading\n\\[foo]: /url \"not a reference\"\n\\&ouml; not a character entity\n") {
        p {
            +"*not emphasized"
            +"*"
            +"\n"
            +"&lt;br/"
            +"&gt;"
            +" "
            +"not a tag"
            +"\n"
            +"[not a link"
            +"]"
            +"("
            +"/foo"
            +")"
            +"\n"
            +"`"
            +"not code"
            +"`"
            +"\n"
            +"1."
            +" "
            +"not a list"
            +"\n"
            +"*"
            +" "
            +"not a list"
            +"\n"
            +"#"
            +" "
            +"not a heading"
            +"\n"
            +"[foo"
            +"]"
            +":"
            +" "
            +"/url"
            +" "
            +"&quot;"
            +"not a reference"
            +"&quot;"
            +"\n"
            +"&amp;ouml;"
            +" "
            +"not a character entity"
        }
    }

    @Test
    fun testBackslashEscapesExample15() = doTest("\\\\*emphasis*\n") {
        p {
            +"\\"
            em {
                +"emphasis"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample16() = doTest("foo\\\nbar\n") {
        p {
            +"foo"
            br()
            +"\n"
            +"bar"
        }
    }

    @Test
    fun testBackslashEscapesExample17() = doTest("`` \\[\\` ``\n") {
        p {
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

    @Test
    fun testBackslashEscapesExample19() = doTest("~~~\n\\[\\]\n~~~\n") {
        pre {
            code {
                +"\\[\\]"
                +"\n"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample20() = doTest("<http://example.com?find=\\*>\n") {
        p {
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
        p {
            a {
                href = "/bar*"
                title = "ti*tle"
                +"foo"
            }
        }
    }

    @Test
    // FIXME I cannot resolve this situation
    @Ignore
    fun testBackslashEscapesExample23() = doTest("[foo]\n\n[foo]: /bar\\* \"ti\\*tle\"\n") {
        p {
            a {
                href = "/bar*"
                title = "ti*tle"
                +"foo"
            }
        }
    }

    @Test
    fun testBackslashEscapesExample24() = doTest("``` foo\\+bar\nfoo\n```\n") {
        pre {
            code {
                className = ClassName("language-foo+bar")
                +"foo"
                +"\n"
            }
        }
    }

    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample25() = doTest("&nbsp; &amp; &copy; &AElig; &Dcaron;\n&frac34; &HilbertSpace; &DifferentialD;\n&ClockwiseContourIntegral; &ngE;\n") {
        p {
            +"  &amp; © Æ Ď\n¾ ℋ ⅆ\n∲ ≧̸"
        }
    }

    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample26() = doTest("&#35; &#1234; &#992; &#0;\n") {
        p {
            +"# Ӓ Ϡ �"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample27() = doTest("&#X22; &#XD06; &#xcab;\n") {
        p {
            +"&quot;"
            +" "
            +"ആ"
            +" "
            +"ಫ"
        }
    }

    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample28() = doTest(
        "&nbsp &x; &#; &#x;\n&#87654321;\n&#abcdef0;\n&ThisIsNotDefined; &hi?;\n",
    ) {
        p {
            +"&amp;nbsp &amp;x; &amp;#; &amp;#x;\n&amp;#87654321;\n&amp;#abcdef0;\n&amp;ThisIsNotDefined; &amp;hi?;"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample29() = doTest(
        markdown = "&copy\n",
    ) {
        p {
            +"&amp;copy"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample30() = doTest(markdown = "&MadeUpEntity;\n") {
        p {
            +"&amp;MadeUpEntity;"
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
        p {
            a {
                href = "/f%C3%B6%C3%B6"
                title = "föö"
                +"foo"
            }
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testEntityAndNumericCharacterReferencesExample33() = doTest(
        markdown = "[foo]\n\n[foo]: /f&ouml;&ouml; \"f&ouml;&ouml;\"\n",
    ) {
        p {
            a {
                href = "/f%C3%B6%C3%B6"
                title = "föö"
                +"foo"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample34() = doTest(
        markdown = "``` f&ouml;&ouml;\nfoo\n```\n",
    ) {
        pre {
            code {
                className = ClassName("language-föö")
                +"foo"
                +"\n"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample35() = doTest(
        markdown = "`f&ouml;&ouml;`\n",
    ) {
        p {
            code {
                +"f&amp;ouml;&amp;ouml;"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample36() = doTest(
        markdown = "    f&ouml;f&ouml;\n",
    ) {
        pre {
            code {
                +"f&amp;ouml;f&amp;ouml;"
                +"\n"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample37() = doTest(
        markdown = "&#42;foo&#42;\n*foo*\n",
    ) {
        p {
            +"*foo*"
            +"\n"
            em {
                +"foo"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample38() = doTest(
        markdown = "&#42; foo\n\n* foo\n",
    ) {
        p {
            +"*"
            +" "
            +"foo"
        }
        ul {
            li {
                +"foo"
            }
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample39() = doTest(
        markdown = "foo&#10;&#10;bar\n",
    ) {
        p {
            +"foo\n\nbar"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample40() = doTest(
        markdown = "&#9;foo\n",
    ) {
        p {
            +"\tfoo"
        }
    }

    @Test
    fun testEntityAndNumericCharacterReferencesExample41() = doTest(
        markdown = "[a](url &quot;tit&quot;)\n",
    ) {
        p {
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
        p {
            +"+++"
        }
    }

    @Test
    fun testThematicBreaksExample45() = doTest(
        markdown = "===\n",
    ) {
        p {
            +"==="
        }
    }

    @Test
    fun testThematicBreaksExample46() = doTest(
        markdown = "--\n**\n__\n",
    ) {
        p {
            +"--"
            +"\n"
            +"*"
            +"*"
            +"\n"
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
        p {
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
        p {
            +"_"
            +" _ "
            +"_"
            +" _ a"
        }
        p {
            +"a------"
        }
        p {
            +"---a---"
        }
    }

    @Test
    fun testThematicBreaksExample56() = doTest(
        markdown = " *-*\n",
    ) {
        p {
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
        p {
            +"Foo"
        }
        hr()
        p {
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
        p {
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
        p {
            +"#######"
            +" "
            +"foo"
        }
    }

    @Test
    fun testATXHeadingsExample64() = doTest(
        markdown = "#5 bolt\n\n#hashtag\n",
    ) {
        p {
            +"#5 bolt"
        }
        p {
            +"#hashtag"
        }
    }

    @Test
    fun testATXHeadingsExample65() = doTest(
        markdown = "\\## foo\n",
    ) {
        p {
            +"##"
            +" "
            +"foo"
        }
    }

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
            +"*baz*"
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
        p {
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
            +"###"
        }
        h2 {
            +"foo"
            +" "
            +"###"
        }
        h1 {
            +"foo"
            +" "
            +"#"
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
        p {
            +"Foo bar"
        }
        h1 {
            +"baz"
        }
        p {
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
        p {
            +"Foo\n---"
        }
    }

    @Test
    fun testSetextHeadingsExample88() = doTest(
        markdown = "Foo\n= =\n\nFoo\n--- -\n",
    ) {
        p {
            +"Foo"
            +"\n"
            +"="
            +" "
            +"="
        }
        p {
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

    @Test
    fun testSetextHeadingsExample91() = doTest(
        markdown = "`Foo\n----\n`\n\n<a title=\"a lot\n---\nof dashes\"/>\n",
    ) {
        h2 {
            +"`"
            +"Foo"
        }
        p {
            +"`"
        }
        h2 {
            +"&lt;"
            +"a title="
            +"&quot;"
            +"a lot"
        }
        p {
            +"of dashes"
            +"&quot;"
            +"/"
            +"&gt;"
        }
    }

    @Test
    fun testSetextHeadingsExample92() = doTest(
        markdown = "> Foo\n---\n",
    ) {
        blockquote {
            p {
                +"Foo"
            }
        }
        hr()
    }

    @Test
    fun testSetextHeadingsExample93() = doTest(
        markdown = "> foo\nbar\n===\n",
    ) {
        blockquote {
            p {
                +"foo"
                +"\n"
                +"bar"
                +"\n"
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
        p {
            +"Baz"
        }
    }

    @Test
    fun testSetextHeadingsExample97() = doTest(
        markdown = "\n====\n",
    ) {
        p {
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
            p {
                +"foo"
            }
        }
        hr()
    }

    @Test
    fun testSetextHeadingsExample102() = doTest(
        markdown = "\\> foo\n------\n",
    ) {
        h2 {
            +"&gt;"
            +" "
            +"foo"
        }
    }

    @Test
    fun testSetextHeadingsExample103() = doTest(
        markdown = "Foo\n\nbar\n---\nbaz\n",
    ) {
        p {
            +"Foo"
        }
        h2 {
            +"bar"
        }
        p {
            +"baz"
        }
    }

    @Test
    fun testSetextHeadingsExample104() = doTest(
        markdown = "Foo\nbar\n\n---\n\nbaz\n",
    ) {
        p {
            +"Foo"
            +"\n"
            +"bar"
        }
        hr()
        p {
            +"baz"
        }
    }

    @Test
    fun testSetextHeadingsExample105() = doTest(
        markdown = "Foo\nbar\n* * *\nbaz\n",
    ) {
        p {
            +"Foo"
            +"\n"
            +"bar"
        }
        hr()
        p {
            +"baz"
        }
    }

    @Test
    fun testSetextHeadingsExample106() = doTest(
        markdown = "Foo\nbar\n\\---\nbaz\n",
    ) {
        p {
            +"Foo"
            +"\n"
            +"bar"
            +"\n"
            +"---"
            +"\n"
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
                p {
                    +"foo"
                }
                p {
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
                p {
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

    @Test
    fun testIndentedCodeBlocksExample110() = doTest(
        markdown = "    <a/>\n    *hi*\n\n    - one\n",
    ) {
        pre {
            code {
                +"&lt;a/&gt;"
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
        p {
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
        p {
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

    @Test
    fun testFencedCodeBlocksExample119() = doTest(
        markdown = "```\n<\n >\n```\n",
    ) {
        pre {
            code {
                +"&lt;"
                +"\n"
                +" &gt;"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample120() = doTest(
        markdown = "~~~\n<\n >\n~~~\n",
    ) {
        pre {
            code {
                +"&lt;"
                +"\n"
                +" &gt;"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample121() = doTest(
        markdown = "``\nfoo\n``\n",
    ) {
        p {
            code {
                +"foo"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample122() = doTest(
        markdown = "```\naaa\n~~~\n```\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"~~~"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample123() = doTest(
        markdown = "~~~\naaa\n```\n~~~\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"```"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample124() = doTest(
        markdown = "````\naaa\n```\n``````\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"```"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample125() = doTest(
        markdown = "~~~~\naaa\n~~~\n~~~~\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"~~~"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample126() = doTest(
        markdown = "```\n",
    ) {
        pre {
            code()
        }
    }

    @Test
    fun testFencedCodeBlocksExample127() = doTest(
        markdown = "`````\n\n```\naaa\n",
    ) {
        pre {
            code {
                +"\n"
                +"```"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample128() = doTest(
        markdown = "> ```\n> aaa\n\nbbb\n",
    ) {
        blockquote {
            pre {
                code {
                    +"aaa"
                    +"\n"
                }
            }
        }
        p {
            +"bbb"
        }
    }

    @Test
    fun testFencedCodeBlocksExample129() = doTest(
        markdown = "```\n\n  \n```\n",
    ) {
        pre {
            code {
                +"\n"
                +"  "
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample130() = doTest(
        markdown = "```\n```\n",
    ) {
        pre {
            code()
        }
    }

    @Test
    fun testFencedCodeBlocksExample131() = doTest(
        markdown = " ```\n aaa\naaa\n```\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample132() = doTest(
        markdown = "  ```\naaa\n  aaa\naaa\n  ```\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"aaa"
                +"\n"
                +"aaa"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample133() = doTest(
        markdown = "   ```\n   aaa\n    aaa\n  aaa\n   ```\n",
    ) {
        pre {
            code {
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

    @Test
    fun testFencedCodeBlocksExample135() = doTest(
        markdown = "```\naaa\n  ```\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample136() = doTest(
        markdown = "   ```\naaa\n  ```\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample137() = doTest(
        markdown = "```\naaa\n    ```\n",
    ) {
        pre {
            code {
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
        p {
            code()
            +"\naaa"
        }
    }

    @Test
    fun testFencedCodeBlocksExample139() = doTest(
        markdown = "~~~~~~\naaa\n~~~ ~~\n",
    ) {
        pre {
            code {
                +"aaa"
                +"\n"
                +"~~~ ~~"
                +"\n"
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample140() = doTest(
        markdown = "foo\n```\nbar\n```\nbaz\n",
    ) {
        p {
            +"foo"
        }
        pre {
            code {
                +"bar"
                +"\n"
            }
        }
        p {
            +"baz"
        }
    }

    @Test
    fun testFencedCodeBlocksExample141() = doTest(
        markdown = "foo\n---\n~~~\nbar\n~~~\n# baz\n",
    ) {
        h2 {
            +"foo"
        }
        pre {
            code {
                +"bar"
                +"\n"
            }
        }
        h1 {
            +"baz"
        }
    }

    @Test
    fun testFencedCodeBlocksExample142() = doTest(
        markdown = "```ruby\ndef foo(x)\n  return 3\nend\n```\n",
    ) {
        pre {
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

    @Test
    fun testFencedCodeBlocksExample143() = doTest(
        markdown = "~~~~    ruby startline=3 \$%@#\$\ndef foo(x)\n  return 3\nend\n~~~~~~~\n",
    ) {
        pre {
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

    @Test
    fun testFencedCodeBlocksExample144() = doTest(
        markdown = "````;\n````\n",
    ) {
        pre {
            code {
                className = ClassName("language-;")
            }
        }
    }

    @Test
    fun testFencedCodeBlocksExample145() = doTest(
        markdown = "``` aa ```\nfoo\n",
    ) {
        p {
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

    @Test
    fun testFencedCodeBlocksExample147() = doTest(
        markdown = "```\n``` aaa\n```\n",
    ) {
        pre {
            code {
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
                    p {
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
        p {
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
        p {
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
                __html = "<pre language=\"haskell\"><code>\nimport Text.HTML.TagSoup\n\nmain :: IO ()\nmain = print \$ parseTags tags\n</code></pre>"
            }
        }
        p {
            +"okay"
        }
    }

    @Test
    fun testHTMLBlocksExample170() = doTest(
        markdown = "<script type=\"text/javascript\">\n// JavaScript example\n\ndocument.getElementById(\"demo\").innerHTML = \"Hello JavaScript!\";\n</script>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<script type=\"text/javascript\">\n// JavaScript example\n\ndocument.getElementById(\"demo\").innerHTML = \"Hello JavaScript!\";\n</script>"
            }
        }
        p {
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
        markdown = "<style\n  type=\"text/css\">\nh1 {color:red;}\n\np {color:blue;}\n</style>\nokay\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<style\n  type=\"text/css\">\nh1 {color:red;}\n\np {color:blue;}\n</style>"
            }
        }
        p {
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
        p {
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
        p {
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
        p {
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
        p {
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
        p {
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
                __html = "<![CDATA[\nfunction matchwo(a,b)\n{\n  if (a < b && a < 0) then {\n    return 1;\n\n  } else {\n\n    return 0;\n  }\n}\n]]>"
            }
        }
        p {
            +"okay"
        }
    }

    /**
     * FIXME: Same case in [testHTMLBlocksExample152]
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample183() = doTest(
        markdown = "  <!-- foo -->\n\n    <!-- foo -->\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "  <!-- foo -->\n<pre><code>&lt;!-- foo --&gt;\n</code></pre>\n"
            }
        }
    }

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
                +"&lt;div&gt;"
                +"\n"
            }
        }
    }

    @Test
    fun testHTMLBlocksExample185() = doTest(
        markdown = "Foo\n<div>\nbar\n</div>\n",
    ) {
        p {
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

    @Test
    fun testHTMLBlocksExample187() = doTest(
        markdown = "Foo\n<a href=\"bar\">\nbaz\n",
    ) {
        p {
            +"Foo"
            +"\n"
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
     */
    @Test
    @Ignore
    fun testHTMLBlocksExample191() = doTest(
        markdown = "<table>\n\n  <tr>\n\n    <td>\n      Hi\n    </td>\n\n  </tr>\n\n</table>\n",
    ) {
        div {
            dangerouslySetInnerHTML = jso {
                __html = "<table>\n  <tr>\n<pre><code>&lt;td&gt;\n  Hi\n&lt;/td&gt;\n</code></pre>\n  </tr>\n</table>\n"
            }
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample192() = doTest(
        markdown = "[foo]: /url \"title\"\n\n[foo]\n",
    ) {
        p {
            a {
                href = "/url"
                title = "title"
                +"foo"
            }
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample193() = doTest(
        markdown = "   [foo]: \n      /url  \n           'the title'  \n\n[foo]\n",
    ) {
        p {
            a {
                href = "/url"
                title = "the title"
                +"foo"
            }
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample194() = doTest(
        markdown = "[Foo*bar\\]]:my_(url) 'title (with parens)'\n\n[Foo*bar\\]]\n",
    ) {
        p {
            a {
                href = "my_(url)"
                title = "title (with params)"
                +"Foo*bar]"
            }
        }
    }

    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample195() = doTest(
        markdown = "[Foo bar]:\n<my url>\n'title'\n\n[Foo bar]\n",
    ) {
        p {
            a {
                href = "my%20url"
                title = "title"
                +"Foo bar"
            }
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample196() = doTest(
        markdown = "[foo]: /url '\ntitle\nline1\nline2\n'\n\n[foo]\n",
    ) {
        p {
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
        p {
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
        p {
            +"with blank line"
            +"'"
        }
        p {
            +"["
            +"foo"
            +"]"
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample198() = doTest(
        markdown = "[foo]:\n/url\n\n[foo]\n",
    ) {
        p {
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
        p {
            +"["
            +"foo"
            +"]"
            +":"
        }
        p {
            +"["
            +"foo"
            +"]"
        }
    }

    /**
     * FIXME: Same case in [testBackslashEscapesExample23]
     */
    @Test
    @Ignore
    fun testLinkReferenceDefinitionsExample200() = doTest(
        markdown = "[foo]: <>\n\n[foo]\n",
    ) {
        p {
            a {
                href = ""
                +"foo"
            }
        }
    }
}
