import java.io.File
import java.nio.charset.Charset

private object Day14 {
    fun firstStar(input: List<String>): Long = polymerizationResult(input, steps = 10)
    fun secondStar(input: List<String>): Long = polymerizationResult(input, steps = 40)

    private fun polymerizationResult(
        input: List<String>,
        steps: Int
    ): Long {
        val (template, insertionRules) = input[0] to input.drop(2)
        val rules = insertionRules.associate { rule -> rule.split(" -> ").let { it[0] to it[1] } }

        val eachPolymerCount = template.windowed(2)
            .groupingBy { it }
            .eachCount()
            .map { it.key to it.value.toLong() }
            .associate { it }.toMutableMap()

        val numberOfOccurrences = template.groupingBy { it }
            .eachCount()
            .map { it.key to it.value.toLong() }
            .associate { it }.toMutableMap()

        for (step in 1..steps) {
            val temp = mutableMapOf<String, Long>()

            eachPolymerCount
                .filter { (key) -> rules.containsKey(key) }
                .filter { (_, count) -> count > 0 }
                .forEach { (polymer, count) ->
                    val (leftMolecule, rightMolecule) = polymer.toList()
                    val newMolecule = rules.getValue(polymer)
                    val addCountToExisting: (key: Any, existing: Long?) -> Long = { _, existing -> (existing ?: 0) + count }
                    temp.compute("$leftMolecule$newMolecule", addCountToExisting)
                    temp.compute("$newMolecule$rightMolecule", addCountToExisting)
                    numberOfOccurrences.compute(newMolecule[0], addCountToExisting)
                    temp.compute(polymer) { _, existing -> (existing ?: 0) - count }
                }

            temp.forEach { (key, diff) -> eachPolymerCount[key] = eachPolymerCount.getOrDefault(key, 0) + diff }
        }

        return numberOfOccurrences.values.let { it.maxOrNull()!! - it.minOrNull()!! }
    }
}

fun main() {
    val input = File("resources", "day14.txt").readLines(Charset.defaultCharset())
    println(Day14.firstStar(input))
    println(Day14.secondStar(input))
}
