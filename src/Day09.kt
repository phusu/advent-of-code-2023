fun main() {

    fun calculateSingleDifferences(ints: List<Int>): MutableList<Int> {
        val differences = mutableListOf<Int>()
        for (i in 1..<ints.size) {
            differences.add(ints[i] - ints[i - 1])
        }
        return differences
    }

    fun calculateDifferences(numberList: MutableList<Int>): MutableList<MutableList<Int>> {
        val differencesList = mutableListOf<MutableList<Int>>()
        differencesList.add(numberList)
        var i = 0
        while (true) {
            val differences = calculateSingleDifferences(differencesList[i])
            differencesList.add(differences)
            ++i
            if (differences.all { it == 0 }) {
                break
            }
        }
        return differencesList
    }

    fun extrapolate(differencesList: MutableList<MutableList<Int>>): Int {
        var i = differencesList.size - 2
        while (i >= 0) {
            val sublist = differencesList[i]
            sublist.add(sublist.last() + differencesList[i + 1].last())
            --i
        }
        return differencesList.first().last()
    }

    fun extrapolateBackwards(differencesList: MutableList<MutableList<Int>>): Int {
        var i = differencesList.size - 2
        while (i >= 0) {
            val sublist = differencesList[i]
            sublist.add(0, sublist.first() - differencesList[i + 1].first())
            --i
        }
        return differencesList.first().first()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val numberList = line.split(" ").map { it.toInt() }.toMutableList()
            val differencesList = calculateDifferences(numberList)

            extrapolate(differencesList)
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            val numberList = line.split(" ").map { it.toInt() }.toMutableList()
            val differencesList = calculateDifferences(numberList)

            extrapolateBackwards(differencesList)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
