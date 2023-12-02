fun main() {
    data class GameSet(val redCubes: Int, val greenCubes: Int, val blueCubes: Int)
    data class Game(val id: Int, val sets: Set<GameSet>)

    val bagLimits = GameSet(12, 13, 14)

    fun parseSet(set: String): GameSet {
        val redRegex = """(\d+) red""".toRegex()
        val greenRegex = """(\d+) green""".toRegex()
        val blueRegex = """(\d+) blue""".toRegex()
        val redCubes: Int = redRegex.find(set)?.groupValues?.get(1)?.toInt() ?: 0

        val greenCubes: Int = greenRegex.find(set)?.groupValues?.get(1)?.toInt() ?: 0
        val blueCubes: Int = blueRegex.find(set)?.groupValues?.get(1)?.toInt() ?: 0
        return GameSet(redCubes, greenCubes, blueCubes)
    }

    fun parseLine(it: String): Game {
        val id = it.substringBefore(':').split(' ').last().toInt()
        val sets = it.substringAfter(':').split(';')
        val gameSets: Set<GameSet> = sets.map { set -> parseSet(set) }.toSet()
        return Game(id, gameSets)
    }

    fun parseInput(input: List<String>): Set<Game> {
        return input.map { parseLine(it) }.toSet()
    }

    fun isPossible(game: Game): Boolean {
        return game.sets.all { set ->
            set.redCubes <= bagLimits.redCubes && set.greenCubes <= bagLimits.greenCubes && set.blueCubes <= bagLimits.blueCubes
        }
    }

    fun minimumNumberOfCubes(game: Game): GameSet {
        val redCubesNeeded = game.sets.maxOf { it.redCubes }
        val greenCubesNeeded = game.sets.maxOf { it.greenCubes }
        val blueCubesNeeded = game.sets.maxOf { it.blueCubes }
        return GameSet(redCubesNeeded, greenCubesNeeded, blueCubesNeeded)
    }

    fun powerOfCubes(gameSet: GameSet): Int {
        return gameSet.redCubes * gameSet.greenCubes * gameSet.blueCubes
    }

    fun part1(input: List<String>): Int {
        val games = parseInput(input)
        return games.filter { game -> isPossible(game) }.sumOf { game -> game.id }
    }

    fun part2(input: List<String>): Int {
        val games = parseInput(input)
        return games.map { game -> minimumNumberOfCubes(game) }
                .sumOf { gameSet -> powerOfCubes(gameSet) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day02_test")
    check(part1(testInput1) == 8)
    check(part2(testInput1) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
