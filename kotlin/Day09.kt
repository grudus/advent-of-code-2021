import java.io.File
import java.nio.charset.Charset

private object Day09 {
    private data class Point(val row: Int, val col: Int, val height: Int)

    fun firstStar(heightmap: List<List<Int>>): Int = findLowPoints(heightmap).sumOf { it.height + 1 }

    fun secondStar(heightmap: List<List<Int>>): Long {
        val lowPoints = findLowPoints(heightmap)
        val alreadyVisited = mutableSetOf<Point>()

        fun findBasins(currentPoint: Point, pointsInBasin: Set<Point>): Set<Point> {
            alreadyVisited += currentPoint

            val neighboursToCheck = findNeighbours(heightmap, currentPoint.row, currentPoint.col)
                .filter { it.height > currentPoint.height && it !in alreadyVisited && it.height != 9 }

            if (neighboursToCheck.isEmpty()) return pointsInBasin

            return neighboursToCheck.map { findBasins(it, pointsInBasin + it) }
                .flatten().toSet()
        }

        return lowPoints.map { point -> findBasins(point, setOf(point)).size }
            .sorted()
            .takeLast(3)
            .fold(1) { acc, num -> acc * num }
    }

    private fun findLowPoints(heightmap: List<List<Int>>): List<Point> =
        heightmap.mapIndexed { row, cols ->
            cols.mapIndexed { col, height ->
                val neighbours = findNeighbours(heightmap, row, col)
                val isLowPoint = neighbours.all { it.height > height }
                if (isLowPoint) Point(row, col, height) else null
            }
        }.flatten().filterNotNull()

    private fun findNeighbours(heightmap: List<List<Int>>, currRow: Int, currCol: Int): List<Point> =
        listOf(currRow + 1 to currCol, currRow - 1 to currCol, currRow to currCol + 1, currRow to currCol - 1)
            .filter { (row, col) -> row >= 0 && col >= 0 && row < heightmap.size && col < heightmap[0].size }
            .map { (row, col) -> Point(row, col, heightmap[row][col]) }
}

fun main() {
    val heightmap = File("resources", "day09.txt").readLines(Charset.defaultCharset())
        .map { line -> line.toList().map { it.digitToInt() } }
    println(Day09.firstStar(heightmap))
    println(Day09.secondStar(heightmap))
}
