import java.io.File
import java.nio.charset.Charset
import kotlin.math.abs

private typealias Grid = List<List<Int>>

object Day15 {
    private data class Node(val col: Int, val row: Int, var totalRisk: Int) {
        override fun equals(other: Any?): Boolean = (other!! as Node).let { it.col == col && it.row == row }
        override fun hashCode() = col.hashCode() + row.hashCode()
    }

    fun firstStar(grid: Grid): Int = aStarFindShortestPath(grid)
    fun secondStar(grid: Grid): Int = aStarFindShortestPath(expand(grid))

    private fun aStarFindShortestPath(grid: Grid): Int {
        fun heuristic(from: Node, to: Node) = abs(from.row - to.row) + abs(from.col - to.col)

        val targetNode = Node(grid[0].lastIndex, grid.lastIndex, grid.last().last())
        val startNode = Node(0, 0, 0)

        val toVisitQueue = mutableListOf(startNode)
        val costSoFar = mutableMapOf(startNode to 0)

        while (toVisitQueue.isNotEmpty()) {
            val current = toVisitQueue.minByOrNull { it.totalRisk }!!
            toVisitQueue.remove(current)

            if (current == targetNode) {
                return costSoFar[current]!!
            }

            findAdjacent(grid, current.row, current.col)
                .forEach { next ->
                    val newCost = costSoFar[current]!! + next.totalRisk

                    if (next !in costSoFar || newCost < costSoFar[next]!!) {
                        costSoFar[next] = newCost
                        next.totalRisk = newCost + heuristic(targetNode, next)
                        toVisitQueue += next
                    }
                }
        }

        return costSoFar.getValue(targetNode)
    }

    private fun findAdjacent(grid: Grid, row: Int, col: Int): List<Node> =
        listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
            .map { (dRow, dCol) -> row + dRow to col + dCol }
            .filter { (y, x) -> y >= 0 && x >= 0 && y < grid.size && x < grid[0].size }
            .map { (y, x) -> Node(x, y, grid[y][x]) }

    private fun expand(grid: Grid): Grid {
        val incrementalDiffs: List<Int> = (0..9).toList() + (1..9).toList()
        return (0 until grid.size * 5).map { row ->
            (0 until grid[0].size * 5).map { col ->
                val diff: Int = (row / grid.size) + (col / grid[0].size)
                val baseValue = grid[row % grid.size][col % grid[0].size]
                val newValue = incrementalDiffs[baseValue + diff]
                newValue
            }
        }
    }
}

fun main() {
    val input = File("resources", "day15.txt").readLines(Charset.defaultCharset())
        .map { row -> row.map { it.digitToInt() } }
    println(Day15.firstStar(input))
    println(Day15.secondStar(input))
}
