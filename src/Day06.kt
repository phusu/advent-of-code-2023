fun main() {
    data class Race(val time: Long, val distance: Long)

    fun parseInput(input: List<String>): List<Race> {
        val times = input[0].substringAfter(":").trim().split("""\s+""".toRegex()).map { it.trim().toLong() }
        val distances = input[1].substringAfter(":").trim().split("""\s+""".toRegex()).map { it.trim().toLong() }
        val races = mutableListOf<Race>()
        for (i in times.indices) {
            races.add(Race(times[i], distances[i]))
        }
        return races
    }

    fun parseInputCombined(input: List<String>): Race {
        val time = input[0].substringAfter(":").trim().replace("""\s+""".toRegex(), "").toLong()
        val distance = input[1].substringAfter(":").trim().replace("""\s+""".toRegex(), "").toLong()
        return Race(time, distance)
    }

    fun getPossibleCombinationsForRace(race: Race): Long {
        var chargeTime = 1L
        var possibleCombinations = 0L

        while (chargeTime < race.time) {
            val remainingTime = race.time - chargeTime
            val travelledDistance = remainingTime * chargeTime * 1

            if (travelledDistance > race.distance) {
                ++possibleCombinations
            }
            ++chargeTime
        }
        return possibleCombinations
    }

    fun part1(input: List<String>): Long {
        val races = parseInput(input)
        return races.map { getPossibleCombinationsForRace(it) }
            .reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val race = parseInputCombined(input)
        return getPossibleCombinationsForRace(race)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
