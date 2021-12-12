import java.io.File
import java.nio.charset.Charset.defaultCharset

object Day12 {
    private data class Graph<T>(val nodes: Map<T, List<T>>)

    fun firstStar(cavesLinks: List<String>): Int =
        buildGraph(cavesLinks).countPaths(
            from = "start",
            to = "end",
            canBeVisitedMultipleTimes = { it.all(Char::isUpperCase) },
            canBeVisitedTwice = { false }
        )

    fun secondStar(cavesLinks: List<String>): Int =
        buildGraph(cavesLinks).countPaths(
            from = "start",
            to = "end",
            canBeVisitedMultipleTimes = { it.all(Char::isUpperCase) },
            canBeVisitedTwice = { it.all(Char::isLowerCase) && it != "start" && it != "end" }
        )

    private fun buildGraph(cavesLinks: List<String>): Graph<String> =
        cavesLinks.fold(emptyMap<String, List<String>>()) { vertices, link ->
            val (from, to) = link.split("-")
            val fromAdj = vertices.getOrDefault(from, listOf()) + to
            val toAdj = vertices.getOrDefault(to, listOf()) + from
            vertices + (from to fromAdj) + (to to toAdj)
        }.let { Graph(it) }

    private fun <T> Graph<T>.countPaths(
        from: T,
        to: T,
        canBeVisitedMultipleTimes: (T) -> Boolean,
        canBeVisitedTwice: (T) -> Boolean
    ): Int {
        fun invalidPath(path: List<T>): Boolean =
            path.filter { canBeVisitedTwice(it) }.groupingBy { it }.eachCount().filter { (_, count) -> count >= 2 }.size > 1

        fun allowSecondVisit(path: List<T>): Boolean =
            path.filter { canBeVisitedTwice(it) }.groupingBy { it }.eachCount().all { (_, count) -> count < 2 }

        fun countPaths(
            from: T,
            to: T,
            visited: Set<T>,
            pathList: List<T>,
        ): Int {
            if (invalidPath(pathList)) return 0
            if (from == to) return 1

            return nodes.getValue(from)
                .filter { node ->
                    !visited.contains(node) ||
                    canBeVisitedMultipleTimes(node) ||
                    (canBeVisitedTwice(node) && allowSecondVisit(pathList))
                }
                .sumOf { node -> countPaths(node, to, visited + from, pathList + node) }
        }
        return countPaths(from, to, setOf(), listOf())
    }
}

fun main() {
    val input = File("resources", "day12.txt").readLines(defaultCharset())
    println(Day12.firstStar(input))
    println(Day12.secondStar(input))
}
