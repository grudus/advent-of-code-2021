import java.io.File
import java.nio.charset.Charset

private object Day11 {
    private data class Position(val row: Int, val col: Int)

    fun firstStar(input: List<String>, steps: Int = 100): Int {
        var octopuses: Map<Position, Int> = parseOctopusesPositions(input)
        var numOfFlashes = 0
        for (step in 0 until steps) {
            octopuses = normalIncrease(octopuses)
            octopuses = findAllLights(octopuses)
            numOfFlashes += octopuses.count { it.value == 0 }
        }

        return numOfFlashes
    }

    fun secondStar(input: List<String>): Int {
        var octopuses: Map<Position, Int> = parseOctopusesPositions(input)

        for (step in 0 until Int.MAX_VALUE) {
            octopuses = normalIncrease(octopuses)
            octopuses = findAllLights(octopuses)
            val allFlashes = octopuses.all { it.value == 0 }
            if (allFlashes)
                return step
        }
        throw RuntimeException("Impossibru")
    }

    private fun findAllLights(octopuses: Map<Position, Int>): Map<Position, Int> {
        val tempMap = octopuses.toMutableMap()

        val aboutToLight = tempMap.filter { (_, energy) -> energy > 9 }
        if (aboutToLight.isEmpty()) return octopuses

        aboutToLight.forEach { (position) -> tempMap[position] = 0 }

        aboutToLight.map { findAdjacentPositions(it.key) }
            .flatMap { neighbours -> neighbours.filter { tempMap.getValue(it) > 0 } }
            .forEach { position -> tempMap[position] = tempMap.getValue(position) + 1 }

        return findAllLights(tempMap)
    }

    private fun normalIncrease(octopuses: Map<Position, Int>): Map<Position, Int> {
        val copy = octopuses.toMutableMap()
        copy.forEach { (position) -> copy[position] = copy.getValue(position) + 1 }
        return copy
    }

    private fun findAdjacentPositions(current: Position): List<Position> {
        return (-1..1).flatMap { dRow ->
            (-1..1).map { dCol ->
                Position(current.row + dRow, current.col + dCol)
            }
        }
            .filter { position -> position != current && position.col >= 0 && position.row >= 0 && position.row < 9 && position.col < 9 }
    }

    private fun parseOctopusesPositions(input: List<String>): Map<Position, Int> {
        return (input.indices).flatMap { row ->
            (0 until input[0].length).map { col ->
                Position(row, col) to input[row][col].digitToInt()
            }
        }.associate { it }
    }
}

fun main() {
    val chunks = File("resources", "day11.txt").readLines(Charset.defaultCharset())
    println(Day11.firstStar(chunks))
}
