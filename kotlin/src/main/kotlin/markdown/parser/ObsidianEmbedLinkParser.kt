package markdown.parser

import markdown.type.ObsidianElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.parser.sequentialparsers.RangesListBuilder
import org.intellij.markdown.parser.sequentialparsers.SequentialParser
import org.intellij.markdown.parser.sequentialparsers.TokensCache

class ObsidianEmbedLinkParser : SequentialParser {
    override fun parse(tokens: TokensCache, rangesToGlue: List<IntRange>): SequentialParser.ParsingResult {
        var result = SequentialParser.ParsingResultBuilder()
        val delegateIndices = RangesListBuilder()
        var iterator: TokensCache.Iterator = tokens.RangesListIterator(rangesToGlue)
        while (iterator.type != null) {
            val isTargetFile = iterator.type == MarkdownTokenTypes.EXCLAMATION_MARK &&
                iterator.rawLookup(1) == MarkdownTokenTypes.LBRACKET &&
                iterator.rawLookup(2) == MarkdownTokenTypes.LBRACKET
            if (isTargetFile) {
                // ![>>[<<link]]
                iterator = iterator.advance().advance()
                val target = ObsidianLinkParser.parseTarget(iterator)
                if (target != null) {
                    result = result.withNode(
                        SequentialParser.Node(
                            // Target >>![[link]]<<
                            iterator.index - 2..target.iteratorPosition.index + 1,
                            ObsidianElementTypes.EMBED_LINK,
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
}
