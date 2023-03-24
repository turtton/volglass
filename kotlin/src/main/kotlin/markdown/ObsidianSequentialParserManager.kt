package markdown

import markdown.parser.ObsidianLineBreakParser
import markdown.parser.ObsidianLinkParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.SequentialParserManager

class ObsidianSequentialParserManager(
    private val parentParsers: List<SequentialParser>,
) : SequentialParserManager() {
    override fun getParserSequence(): List<SequentialParser> {
        return listOf(ObsidianLinkParser(), ObsidianLineBreakParser()) + parentParsers
    }
}
