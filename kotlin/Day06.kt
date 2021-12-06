import java.io.File
import java.nio.charset.Charset

private object Day06 {

    fun firstStar(lanternfish: List<Int>) = countLanternfish(lanternfish, 80)
    fun secondStar(lanternfish: List<Int>) = countLanternfish(lanternfish, 256)

    private fun countLanternfish(lanternfish: List<Int>, days: Int): Long {
        val fishCount = (0..9).associateWith { day -> lanternfish.count { day == it }.toLong() }.toMutableMap()
        val nextGenDay = 9
        val respawnDay = 7

        for (day in 1..days) {
            val nextGen = fishCount.getValue(0)
            fishCount[nextGenDay] = nextGen
            fishCount[respawnDay] = nextGen + fishCount.getValue(respawnDay)

            for (count in 0..8) {
                fishCount[count] = fishCount[count + 1] ?: 0
            }
        }
        return fishCount.filterKeys { it != nextGenDay }.values.sum()
    }
}

fun main() {
    val input = File("resources", "day06.txt").readLines(Charset.defaultCharset())
        .get(0).split(",").map { it.toInt() }
    println(Day06.firstStar(input))
    println(Day06.secondStar(input))
}
