package markdown.parser

import markdown.type.ObsidianElementTypes
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.LocalParsingResult
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache

class ObsidianLinkParser : SequentialParser {
    override fun parse(tokens: TokensCache, rangesToGlue: List<IntRange>): SequentialParser.ParsingResult {
        var result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator: TokensCache.Iterator = tokens.RangesListIterator(rangesToGlue)
        while (iterator.type != null) {
            if (iterator.type == MarkdownTokenTypes.LBRACKET && iterator.rawLookup(1) == MarkdownTokenTypes.LBRACKET && iterator.rawLookup(
                    -1,
                ) != MarkdownTokenTypes.EXCLAMATION_MARK
            ) {
                iterator = iterator.advance()
                val target = parseTarget(iterator)
                if (target != null) {
                    result = result.withNode(
                        SequentialParser.Node(
                            // Target [[>>link<<]]
                            iterator.index - 1..target.iteratorPosition.index + 1,
                            ObsidianElementTypes.LINK,
                        ),
                    )
                    iterator = target.iteratorPosition.advance()
                    continue
                }
            }

            delegateIndices.put(iterator.index)
            iterator = iterator.advance()
        }

        return result.withFurtherProcessing(delegateIndices.get())
    }

    companion object {
        fun parseTarget(iterator: TokensCache.Iterator): LocalParsingResult? {
            // Position [[>>l<<ink]]
            val startIndex = iterator.index + 1

            var current = iterator.advance()
            val range = RangesListBuilder()

            var willClose = false
            if (current.type != MarkdownTokenTypes.TEXT) return null
            while (current.type != null) {
                if (current.type == MarkdownTokenTypes.RBRACKET) {
                    if (!willClose) {
                        willClose = true
                    } else {
                        break
                    }
                } else if (willClose) {
                    return null
                } else if (current.type != MarkdownTokenTypes.TEXT) {
                    return null
                }

                range.put(current.index)
                current = current.advance()
            }
            return LocalParsingResult(
                current,
                listOf(SequentialParser.Node(startIndex..current.index, MarkdownElementTypes.LINK_TEXT)),
                range.get(),
            )
        }
    }
}
