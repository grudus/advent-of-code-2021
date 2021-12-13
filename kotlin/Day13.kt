import java.io.File
import java.nio.charset.Charset

object Day13 {
    private data class Point(val x: Int, val y: Int)
    private sealed interface FoldInstruction {
        fun performFold(point: Point): Point
    }

    private data class FoldX(val value: Int) : FoldInstruction {
        override fun performFold(point: Point): Point {
            val (x, y) = point
            val dx = x - value
            return if (x > value) Point(x - dx * 2, y) else point
        }
    }

    private data class FoldY(val value: Int) : FoldInstruction {
        override fun performFold(point: Point): Point {
            val (x, y) = point
            val dy = y - value
            return if (y > value) Point(x, y - dy * 2) else point
        }
    }

    fun firstStar(input: List<String>): Int {
        val (initialPoints, foldInstructions) = parseInput(input)
        return initialPoints.map { foldInstructions[0].performFold(it) }.toSet().size
    }

    fun secondStar(input: List<String>): Int {
        val (initialPoints, foldInstructions) = parseInput(input)

        val result = foldInstructions.fold(initialPoints) { points, fold ->
            points.map { fold.performFold(it) }.toSet()
        }

        showPoints(result)
        return -1
    }

    private fun parseInput(input: List<String>): Pair<Set<Point>, List<FoldInstruction>> {
        val (points, folds) = input.chunked(input.indexOfFirst { it.startsWith("fold") })
        return parsePoints(points) to parseInstructions(folds)
    }

    private fun parsePoints(points: List<String>): Set<Point> = points.filter { it.isNotBlank() }.map {
        val (x, y) = it.split(",")
        Point(x.toInt(), y.toInt())
    }.toSet()

    private fun parseInstructions(instructions: List<String>): List<FoldInstruction> =
        instructions.map { it.removePrefix("fold along ") }.map {
            val (axis, value) = it.split("=")
            if (axis == "x") FoldX(value.toInt()) else FoldY(value.toInt())
        }

    private fun showPoints(points: Set<Point>) {
        val (maxX, maxY) = points.maxOf { it.x } to points.maxOf { it.y }
        for (y in 0..maxY) {
            for (x in 0..maxX) {
                if (Point(x, y) in points) print(" # ")
                else print(" . ")
            }
            println()
        }
    }
}

fun main() {
    val input = File("resources", "day13.txt").readLines(Charset.defaultCharset())
    println(Day13.firstStar(input))
    println(Day13.secondStar(input))
}
