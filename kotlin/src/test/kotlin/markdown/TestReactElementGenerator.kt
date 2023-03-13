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
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.pre
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

    @Test
    // FIXME I cannot resolve this situation
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
}
