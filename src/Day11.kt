import java.lang.Integer.max
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.min

fun main() {
    data class Galaxy(val id: Int, val x: Int, val y: Int)
    class GalaxyPair(val first: Galaxy, val second: Galaxy) {
        fun lengthBetween(): Int {
            return abs(first.x - second.x) + abs(first.y - second.y)
        }
    }

    fun parseInput(input: List<String>): List<Galaxy> {
        val galaxies = mutableListOf<Galaxy>()
        var counter = 0
        input.forEachIndexed() { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    galaxies.add(Galaxy(counter, x, y))
                    ++counter
                }
            }
        }

        return galaxies
    }

    fun getGalaxyPairs(galaxies: List<Galaxy>): List<GalaxyPair> {
        val galaxyPairs = mutableListOf<GalaxyPair>()
        for (i in galaxies.indices) {
            for (j in i + 1 ..< galaxies.size) {
                galaxyPairs.add(GalaxyPair(galaxies[i], galaxies[j]))
            }
        }
        return galaxyPairs
    }

    fun isEmptyColumn(x: Int, galaxies: List<Galaxy>): Boolean {
        return galaxies.none { it.x == x }
    }

    fun isEmptyRow(y: Int, galaxies: List<Galaxy>): Boolean {
        return galaxies.none{ it.y == y }
    }

    fun emptyGalaxiesBetween(galaxies: List<Galaxy>, galaxyPair: GalaxyPair, part2: Boolean = false): BigInteger {
        var counter = BigInteger.ZERO
        val increment: BigInteger = if (part2) {
            BigInteger.valueOf(999999)
        } else {
            BigInteger.ONE
        }
        val minX = min(galaxyPair.first.x+1, galaxyPair.second.x)
        val maxX = max(galaxyPair.first.x+1, galaxyPair.second.x)
        for (x in minX..<maxX) {
            if (isEmptyColumn(x, galaxies)) {
                counter = counter.add(increment)
            }
        }
        val minY = min(galaxyPair.first.y+1, galaxyPair.second.y)
        val maxY = max(galaxyPair.first.y+1, galaxyPair.second.y)
        for (y in minY..<maxY) {
            if (isEmptyRow(y, galaxies)) {
                counter = counter.add(increment)
            }
        }
        return counter
    }

    fun part1(input: List<String>): BigInteger {
        val galaxies = parseInput(input)
        val galaxyPairs = getGalaxyPairs(galaxies)
        return galaxyPairs.sumOf {
            emptyGalaxiesBetween(galaxies, it).add(it.lengthBetween().toBigInteger())
        }
    }

    fun part2(input: List<String>): BigInteger {
        val galaxies = parseInput(input)
        val galaxyPairs = getGalaxyPairs(galaxies)
        return galaxyPairs.sumOf {
            emptyGalaxiesBetween(galaxies, it, true).add(it.lengthBetween().toBigInteger())
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == BigInteger.valueOf(374))

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
