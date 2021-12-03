import java.io.File
import java.nio.charset.Charset

object Day03 {

    fun firstStar(binaryNumbers: List<String>): Int =
        (0 until binaryNumbers[0].length).map { index ->
            val bits = binaryNumbers.groupingBy { binary -> binary[index] }.eachCount()
            if (bits['0']!! > bits['1']!!) Pair("0", "1") else Pair("1", "0")
        }.let {
            val mostCommon = Integer.parseInt(it.joinToString("") { it.first }, 2)
            val leastCommon = Integer.parseInt(it.joinToString("") { it.second }, 2)
            mostCommon * leastCommon
        }

    fun secondStar(binaryNumbers: List<String>): Int {
        val listCopy = mutableListOf(*binaryNumbers.toTypedArray())
        (0 until binaryNumbers[0].length).map { index ->
            val bits = listCopy.groupingBy { binary -> binary[index] }.eachCount()
            if (bits['0']!! > bits['1']!!)
                listCopy.removeIf { it[index] == '1' }
            else
                listCopy.removeIf { it[index] == '0' }
        }

        val listCopy2 = mutableListOf(*binaryNumbers.toTypedArray())
        (0 until listCopy2[0].length).map { index ->
            val bits = listCopy2.groupingBy { binary -> binary[index] }.eachCount()
            if (bits.size == 1) return@map

            if (bits['0']!! <= bits['1']!!)
                listCopy2.removeIf { it[index] == '1' }
            else
                listCopy2.removeIf { it[index] == '0' }
        }

        return Integer.parseInt(listCopy2[0], 2) * Integer.parseInt(listCopy[0], 2)
    }
}

fun main() {
    val input = File("resources", "day03.txt").readLines(Charset.defaultCharset())
    println(Day03.secondStar(input))
    println(Day03.secondStar(input))
}
