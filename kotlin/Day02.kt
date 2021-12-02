import java.io.File
import java.nio.charset.Charset

object Day02 {
    enum class Instruction(val dx: Int, val dy: Int) {
        FORWARD(1, 0), DOWN(0, 1), UP(0, -1)
    }

    enum class BetterInstructions { FORWARD, DOWN, UP }

    data class Position(val horizontal: Int = 0, val depth: Int = 0, val aim: Int = 0) {
        fun nextPosition(instruction: Instruction, value: Int): Position =
            Position(horizontal + instruction.dx * value, depth + instruction.dy * value)

        fun nextPosition(instruction: BetterInstructions, value: Int): Position =
            when (instruction) {
                BetterInstructions.DOWN -> Position(horizontal, depth, aim + value)
                BetterInstructions.UP -> Position(horizontal, depth, aim - value)
                BetterInstructions.FORWARD -> Position(horizontal + value, depth + value * aim, aim)
            }
    }

    fun firstStar(instructions: List<String>): Int =
        instructions.map { it.split(" ") }
            .map { Pair(Instruction.valueOf(it[0].uppercase()), it[1].toInt()) }
            .fold(Position()) { position, (instruction, value) -> position.nextPosition(instruction, value) }
            .let { it.horizontal * it.depth }

    fun secondStar(instructions: List<String>): Int =
        instructions.map { it.split(" ") }
            .map { Pair(BetterInstructions.valueOf(it[0].uppercase()), it[1].toInt()) }
            .fold(Position()) { position, (instruction, value) -> position.nextPosition(instruction, value) }
            .let { it.horizontal * it.depth }
}

fun main() {
    val input = File("resources", "day02.txt").readLines(Charset.defaultCharset())
    println(Day02.firstStar(input))
    println(Day02.secondStar(input))
}
