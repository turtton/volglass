package markdown.parser

import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache

class ObsidianLineBreakParser : SequentialParser {
    override fun parse(tokens: TokensCache, rangesToGlue: List<IntRange>): SequentialParser.ParsingResult {
        var result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator = tokens.RangesListIterator(rangesToGlue)
        var isNormalLine = true
        val specialTokens = mutableListOf<IElementType>()
        while (iterator.type != null) {
            if (iterator.type == MarkdownTokenTypes.EOL) {
                if (isNormalLine) {
                    result = result.withNode(
                        SequentialParser.Node(
                            iterator.index + 1..iterator.index + 1,
                            MarkdownTokenTypes.HARD_LINE_BREAK,
                        ),
                    )
                }
                // reset states
                isNormalLine = true
                specialTokens.clear()
            } else if (iterator.type in
                listOf(
                        MarkdownElementTypes.ATX_1,
                        MarkdownElementTypes.ATX_2,
                        MarkdownElementTypes.ATX_3,
                        MarkdownElementTypes.ATX_4,
                        MarkdownElementTypes.ATX_5,
                        MarkdownElementTypes.ATX_6,
                        MarkdownTokenTypes.HARD_LINE_BREAK,
                        MarkdownTokenTypes.HTML_TAG,
                    )
            ) {
                isNormalLine = false
            } else if (iterator.type in listOf(MarkdownTokenTypes.BACKTICK)) {
                if (specialTokens.contains(iterator.type)) {
                    isNormalLine = false
                } else {
                    specialTokens.add(iterator.type!!)
                }
            }

            delegateIndices.put(iterator.index)
            iterator = iterator.advance()
        }

        return result.withFurtherProcessing(delegateIndices.get())
    }
}
