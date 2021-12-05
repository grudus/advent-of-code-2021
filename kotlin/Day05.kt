import java.io.File
import java.nio.charset.Charset.defaultCharset
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

private data class Point(val x: Int, val y: Int)
private typealias Line = List<Point>

object Day05 {
    private val lineRegex = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")

    fun firstStar(input: List<String>): Int {
        val lines: List<Line> = findLines(input)
            .filter { line -> line[0].x == line[1].x || line[0].y == line[1].y }

        return findNumberOfOverlappingLines(lines)
    }

    fun secondStar(input: List<String>): Int = findNumberOfOverlappingLines(findLines(input))

    private fun findNumberOfOverlappingLines(lines: List<Line>): Int =
        lines.flatten().groupBy { it }.count { it.value.size > 1 }

    private fun findLines(input: List<String>): List<Line> =
        input.map { rawLine ->
            val (x1, y1, x2, y2) = lineRegex.find(rawLine)?.destructured!!.toList().map { it.toInt() }
            when {
                x1 == x2 -> (min(y1, y2)..max(y1, y2)).map { y -> Point(x1, y) }
                y1 == y2 -> (min(x1, x2)..max(x1, x2)).map { x -> Point(x, y1) }
                else -> {
                    val diff = (x1 - x2).absoluteValue
                    val dx = (x2 - x1).sign
                    val dy = (y2 - y1).sign
                    (0..diff).map { i -> Point(x1 + i * dx, y1 + i * dy) }
                }
            }
        }
}

fun main() {
    val input = File("resources", "day05.txt").readLines(defaultCharset())
    println(Day05.firstStar(input))
    println(Day05.secondStar(input))
}
