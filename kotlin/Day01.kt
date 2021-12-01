import java.io.File
import java.nio.charset.Charset.defaultCharset

object Day01 {

    fun firstStar(numbers: List<Int>): Int =
        numbers.zipWithNext().count { (x, y) -> y > x }

    fun secondStar(numbers: List<Int>): Int {
        val slidingWindows = numbers.zipWithNext().zip(numbers.drop(2)) { (x, y), z -> x + y + z }
        return firstStar(slidingWindows)
    }
}

fun main() {
    val input = File("resources", "day01.txt").readLines(defaultCharset())
    println(Day01.firstStar(input.map { it.toInt() }))
    println(Day01.secondStar(input.map { it.toInt() }))
}
