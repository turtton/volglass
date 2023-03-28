package markdown.parser

import markdown.type.ObsidianElementTypes
import markdown.type.ObsidianTokenTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.LocalParsingResult
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache

class TexParser : SequentialParser {
    override fun parse(tokens: TokensCache, rangesToGlue: List<IntRange>): SequentialParser.ParsingResult {
        var result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator: TokensCache.Iterator = tokens.RangesListIterator(rangesToGlue)
        while (iterator.type != null) {
            val isTargetFile = iterator.type == ObsidianTokenTypes.DOLLAR
            if (isTargetFile) {
                val isDualDuller = iterator.rawLookup(1) == ObsidianTokenTypes.DOLLAR
                // $>>$<<math$$
                if (isDualDuller) {
                    iterator = iterator.advance()
                }
                val target = parseTarget(iterator, isDualDuller)
                if (target != null) {
                    val rangeOffset = if (isDualDuller) 1 else 0
                    result = result.withNode(
                        SequentialParser.Node(
                            // Target >>$$link$$<< or >>$link$<<
                            iterator.index - rangeOffset..target.iteratorPosition.index + 1,
                            ObsidianElementTypes.TEX,
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
        fun parseTarget(iterator: TokensCache.Iterator, isDualDuller: Boolean): LocalParsingResult? {
            // Position $$>>m<<ath$$ or $>>m<<ath$
            val startIndex = iterator.index + 1

            var current = iterator.advance()
            val range = RangesListBuilder()

            var found = false
            var willClose = false
            if (!isDualDuller && current.type == MarkdownTokenTypes.WHITE_SPACE) return null
            while (current.type != null) {
                if (current.type == ObsidianTokenTypes.DOLLAR) {
                    if (!willClose) {
                        willClose = true
                        if (!isDualDuller) {
                            found = true
                            break
                        }
                    } else {
                        found = true
                        break
                    }
                } else if (willClose) {
                    return null
                } else if (current.type == MarkdownTokenTypes.EOL && !isDualDuller) {
                    return null
                }
                range.put(current.index)
                current = current.advance()
            }

            return if (found) {
                LocalParsingResult(
                    current,
                    listOf(SequentialParser.Node(startIndex..current.index, MarkdownTokenTypes.TEXT)),
                    range.get(),
                )
            } else {
                null
            }
        }
    }
}
