import java.io.File
import java.nio.charset.Charset
import kotlin.math.abs
import kotlin.math.roundToInt

private object Day07 {

    fun firstStar(crabPositions: List<Int>): Int {
        val nearest = crabPositions.median().roundToInt()
        return crabPositions.sumOf { abs(it - nearest) }
    }

    fun secondStar(crabPositions: List<Int>): Long {
        val nearest = crabPositions.average().toLong()
        return crabPositions.sumOf { (1..abs(it - nearest)).sum() }
    }

    private fun List<Int>.median(): Double {
        val sorted = this.sorted()
        return if (size % 2 == 0) (sorted[size / 2].toDouble() + sorted[size / 2 - 1].toDouble()) / 2
        else sorted[size / 2].toDouble()
    }
}

fun main() {
    val input = File("resources", "day07.txt").readLines(Charset.defaultCharset())
        .get(0).split(",").map { it.toInt() }
    println(Day07.firstStar(input))
    println(Day07.secondStar(input))
}
