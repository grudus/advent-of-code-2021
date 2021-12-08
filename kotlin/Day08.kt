import Day08.WireSegment.*
import java.io.File
import java.nio.charset.Charset

private object Day08 {
    private enum class WireSegment { TOP, TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT, BOTTOM_LEFT, BOTTOM }
    private data class Line(val patterns: List<String>, val output: List<String>)

    val numberFactory: Map<Set<WireSegment>, Int> = mapOf(
        setOf(TOP, TOP_LEFT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT, TOP_RIGHT) to 0,
        setOf(TOP_RIGHT, BOTTOM_RIGHT) to 1,
        setOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_LEFT, BOTTOM) to 2,
        setOf(TOP, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 3,
        setOf(TOP_LEFT, MIDDLE, TOP_RIGHT, BOTTOM_RIGHT) to 4,
        setOf(TOP, TOP_LEFT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 5,
        setOf(TOP, TOP_LEFT, MIDDLE, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT) to 6,
        setOf(TOP, TOP_RIGHT, BOTTOM_RIGHT) to 7,
        setOf(*WireSegment.values()) to 8,
        setOf(TOP, TOP_LEFT, TOP_RIGHT, MIDDLE, BOTTOM_RIGHT, BOTTOM) to 9,
    )

    fun firstStar(input: List<String>): Int =
        input.flatMap { parseLine(it).output }
            .count { it.length in listOf(7, 4, 3, 2) }

    fun secondStar(inputs: List<String>): Int {
        return inputs.sumOf { line ->
            val (patterns, output) = parseLine(line)
            val decoded: Map<Char, WireSegment> = decode(patterns, output)
            parseNumber(decoded, output)
        }
    }

    private fun parseLine(line: String): Line =
        line.split(" | ").map { it.split(Regex("\\s")) }.let { Line(it[0], it[1]) }

    private fun parseNumber(decoded: Map<Char, WireSegment>, output: List<String>): Int =
        output.joinToString("") { digit ->
            val segments = digit.toList().map { decoded.getValue(it) }.toSet()
            numberFactory.getValue(segments).toString()
        }.toInt()

    private fun decode(_patterns: List<String>, output: List<String>): Map<Char, WireSegment> {
        val codeToSegments = mutableMapOf<Char, WireSegment>()
        val patterns: Set<Set<Char>> = _patterns.map { it.toSet() }.toSet() + output.map { it.toSet() }.toSet()

        val one = patterns.find { it.size == 2 }!!
        val seven = patterns.find { it.size == 3 }!!
        val top = (seven - one).toList()[0]
        codeToSegments[top] = TOP

        val four = patterns.find { it.size == 4 }!!
        val nine = patterns.find { it.size == 6 && it.containsAll(four) }!!

        val bottom = ((nine - four) - top).toList()[0]
        codeToSegments[bottom] = BOTTOM

        val three = patterns.find { it.size == 5 && it.containsAll(one) && it.contains(top) && it.contains(bottom) }!!
        val middle = ((three - one) - top - bottom).toList()[0]
        codeToSegments[middle] = MIDDLE

        val topLeft = ((four - one) - middle).toList()[0]
        codeToSegments[topLeft] = TOP_LEFT

        val five = patterns.find { it.size == 5 && it.contains(topLeft) }!!
        val bottomRight = (five - top - topLeft - middle - bottom).toList()[0]
        codeToSegments[bottomRight] = BOTTOM_RIGHT

        val topRight = (one - bottomRight).toList()[0]
        codeToSegments[topRight] = TOP_RIGHT

        val six = patterns.find { it.size == 6 && !it.contains(topRight) }!!
        val bottomLeft = (six - five).toList()[0]
        codeToSegments[bottomLeft] = BOTTOM_LEFT

        return codeToSegments
    }
}

fun main() {
    val input = File("resources", "day08.txt").readLines(Charset.defaultCharset())
    println(Day08.firstStar(input))
    println(Day08.secondStar(input))
}
