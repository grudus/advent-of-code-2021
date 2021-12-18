import java.io.File
import java.nio.charset.Charset
import kotlin.math.max

private object Day17 {
    private data class Range(val from: Int, val to: Int)
    private data class Probe(val dx: Int, val dy: Int, val x: Int, val y: Int)
    private data class Result(val maxHeight: Int, val numOfPossibilities: Int)

    fun firstStar(input: String) = checkVelocities(input).maxHeight
    fun secondStar(input: String) = checkVelocities(input).numOfPossibilities

    private fun checkVelocities(input: String): Result {
        val (xRange, yRange) = parseTargetArea(input)

        var count = 0
        var maxHeight = 0

        for (dy in (yRange.from - 1) until 1000) {
            for (dx in 1 until xRange.to + 1) {

                val validProbe = generateSequence(Probe(dx, dy, 0, 0) to 0) { (probe, maxHeight) ->
                    val nextProbe = probe.copy(
                        x = probe.x + probe.dx,
                        y = probe.y + probe.dy,
                        dx = max(probe.dx - 1, 0),
                        dy = probe.dy - 1
                    )
                    nextProbe to max(maxHeight, probe.y)
                }
                    .takeWhile { (probe, _) -> probe.y >= yRange.from }
                    .find { (probe, _) ->
                        probe.y >= yRange.from && probe.y <= yRange.to &&
                            probe.x >= xRange.from && probe.x <= xRange.to
                    } ?: continue

                count += 1
                maxHeight = max(maxHeight, validProbe.second)
            }
        }
        return Result(maxHeight = maxHeight, numOfPossibilities = count)
    }

    private fun parseTargetArea(input: String): Pair<Range, Range> {
        val (xRaw, yRaw) = input.replace("target area: ", "").split(", ")
        val xRange = xRaw.drop(2).split("..").let { Range(it[0].toInt(), it[1].toInt()) }
        val yRange = yRaw.drop(2).split("..").let { Range(it[0].toInt(), it[1].toInt()) }
        return Pair(xRange, yRange)
    }
}

fun main() {
    val input = File("resources", "day17.txt").readLines(Charset.defaultCharset())[0]

    println(Day17.firstStar(input))
    println(Day17.secondStar(input))
}
