import java.io.File
import java.nio.charset.Charset

object Day04 {

    data class BingoLine(val numbers: Set<Int>) {
        fun checkBingo(drawn: Set<Int>) = drawn.intersect(numbers).size == 5
        fun getUnmarked(drawn: Set<Int>) = numbers - drawn
    }

    data class Board(val lines: List<BingoLine>) {
        fun checkBingo(drawn: Set<Int>) = lines.any { it.checkBingo(drawn) }

        fun sumUnmarked(drawn: Set<Int>): Int =
            lines.flatMap { it.getUnmarked(drawn) }.toSet().sum()
    }

    fun firstStar(lines: List<String>): Int {
        val (numbersToDraw, boards) = parseInput(lines)

        fun playBingo(drawnNumbers: Set<Int>, numbersToDraw: List<Int>): Int {
            val number = numbersToDraw[0]
            val winner = boards.find { it.checkBingo(drawnNumbers + number) }
            if (winner == null)
                return playBingo(drawnNumbers + number, numbersToDraw.drop(1))
            return winner.sumUnmarked(drawnNumbers + number) * number
        }
        return playBingo(emptySet(), numbersToDraw)
    }

    fun secondStar(lines: List<String>): Int {
        val (numbersToDraw, boards) = parseInput(lines)

        fun playBingo(drawnNumbers: Set<Int>, numbersToDraw: List<Int>, losers: List<Board>): Int {
            val number = numbersToDraw[0]
            val winners = losers.filter { it.checkBingo(drawnNumbers + number) }
            if (winners.isEmpty() || losers.size > 1)
                return playBingo(drawnNumbers + number, numbersToDraw.drop(1), losers - winners)
            return losers[0].sumUnmarked(drawnNumbers + number) * number
        }

        return playBingo(emptySet(), numbersToDraw, boards)
    }

    private fun parseInput(inputLines: List<String>): Pair<List<Int>, List<Board>> {
        val numbersToDraw = inputLines[0].split(",").map { it.toInt() }
        val boards = inputLines.asSequence().drop(1).filter { it.isNotBlank() }
            .map { it.trim().split(Regex("\\s+")).map { it.toInt() } }
            .windowed(5, 5)
            .map { board ->
                val lines = (board + transpose(board)).map { BingoLine(it.toSet()) }
                Board(lines)
            }.toList()
        return numbersToDraw to boards
    }

    private fun transpose(matrix: List<List<Int>>): List<List<Int>> {
        val transposed = MutableList(matrix[0].size) { MutableList(matrix.size) { 0 } }
        for (col in 0 until matrix[0].size)
            for (row in matrix.indices)
                transposed[col][row] = matrix[row][col]
        return transposed
    }
}

fun main() {
    val input = File("resources", "day04.txt").readLines(Charset.defaultCharset())
    println(Day04.firstStar(input))
    println(Day04.secondStar(input))
}