import java.io.File
import java.nio.charset.Charset

private object Day10 {
    private sealed class ChunkResult {
        class Corrupted(val char: Char) : ChunkResult()
        class Incomplete(val openedQueue: ArrayDeque<Char>) : ChunkResult()
    }

    fun firstStar(chunks: List<String>): Int {
        val errorScore = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

        return chunks.map { findInvalidCharacter(it) }
            .filterIsInstance<ChunkResult.Corrupted>()
            .sumOf { errorScore.getValue(it.char) }
    }

    fun secondStar(chunks: List<String>): Long =
        chunks.map { findInvalidCharacter(it) }
            .filterIsInstance<ChunkResult.Incomplete>()
            .map { calculateIncompleteSum(it.openedQueue) }
            .sorted()
            .let { it[it.size / 2] }

    private fun calculateIncompleteSum(openedQueue: ArrayDeque<Char>): Long {
        val itemScore = mapOf('(' to 1, '[' to 2, '{' to 3, '<' to 4)
        return openedQueue.reversed().fold(0) { score, item -> score * 5 + itemScore.getValue(item) }
    }

    private fun findInvalidCharacter(chunk: String): ChunkResult {
        val openedQueue = ArrayDeque<Char>()
        val closingItems = mapOf(')' to '(', '>' to '<', ']' to '[', '}' to '{')
        for (item in chunk) {
            if (item in closingItems.keys) {
                val associatedOpeningItem = closingItems.getValue(item)
                val lastOpened = openedQueue.removeLast()
                if (lastOpened == associatedOpeningItem) continue
                else return ChunkResult.Corrupted(item)
            } else openedQueue.addLast(item)
        }
        return ChunkResult.Incomplete(openedQueue)
    }
}

fun main() {
    val chunks = File("resources", "day10.txt").readLines(Charset.defaultCharset())
    println(Day10.firstStar(chunks))
    println(Day10.secondStar(chunks))
}
