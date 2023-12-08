
enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIRS,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND
}

const val ranks = "23456789TJQKA"
const val ranksWithJokers = "J23456789TQKA"

class Game(val hand: String, val bid: Int, withJokers: Boolean): Comparable<Game> {
    val handType = calculateHandType(hand, withJokers)
    private val withJokers = withJokers

    private fun calculateHandType(hand: String, withJokers: Boolean): HandType {
        val rankCounts = hand.groupBy { ranks.indexOf( it ) }
            .mapValues { (_, cards) -> cards.size }
            .toList()
            .sortedWith(
                compareByDescending<Pair<Int,Int>> { it.second }.thenByDescending { it.first })

        if (withJokers) {
            val hasJokers: Boolean = hand.any { it == 'J' }
            if (hasJokers) {
                val withoutJokers = rankCounts.filter { it.first != 9 }
                val highestIndexWithoutJoker: Int = if (withoutJokers.isEmpty()) {
                    12
                } else {
                    withoutJokers.maxByOrNull { it.second }!!.first
                }
                val handWithJokersReplaced = hand.replace('J', ranks[highestIndexWithoutJoker])
                val rankCountsWithJokers = handWithJokersReplaced.groupBy { ranksWithJokers.indexOf(it) }
                    .mapValues { (_, cards) -> cards.size }
                    .toList()
                    .sortedByDescending { it.second }

                val calculatedHandType = calculateHandType(rankCountsWithJokers)
                return calculatedHandType
            }
        }

        return calculateHandType(rankCounts)
    }

    private fun calculateHandType(rankCounts: List<Pair<Int, Int>>): HandType {
        val handType: HandType = if (rankCounts.any { it.second == 5 }) {
            HandType.FIVE_OF_A_KIND
        } else if (rankCounts.any { it.second == 4 }) {
            HandType.FOUR_OF_A_KIND
        } else if (rankCounts.any { it.second == 3 } && rankCounts.any { it.second == 2 }) {
            HandType.FULL_HOUSE
        } else if (rankCounts.any { it.second == 3 }) {
            HandType.THREE_OF_A_KIND
        } else if (rankCounts.count { it.second == 2 } == 2) {
            HandType.TWO_PAIRS
        } else if (rankCounts.any { it.second == 2 }) {
            HandType.ONE_PAIR
        } else {
            HandType.HIGH_CARD
        }
        return handType
    }

    override fun compareTo(other: Game): Int {
        if (this.handType < other.handType) {
            return -1
        } else if (this.handType > other.handType) {
            return 1
        } else {
            for (i in 0..<5) {
                val card: Int
                val otherCard: Int
                if (withJokers) {
                    card = ranksWithJokers.indexOf(this.hand[i])
                    otherCard = ranksWithJokers.indexOf(other.hand[i])
                } else {
                    card = ranks.indexOf(this.hand[i])
                    otherCard = ranks.indexOf(other.hand[i])
                }
                if (card < otherCard) {
                    return -1
                } else if (card > otherCard) {
                    return 1
                }
            }
            return 0
        }
    }
}

fun main() {

    fun parseInput(input: List<String>, withJokers: Boolean = false): List<Game> {
        return input.map {
            val (hand, bid) = it.split(" ")
            Game(hand, bid.toInt(), withJokers)
        }
    }

    fun part1(input: List<String>): Int {
        val games = parseInput(input).sorted()
        return games.mapIndexed { index, game -> (index+1) * game.bid }
            .sum()
    }

    fun part2(input: List<String>): Int {
        val games = parseInput(input, true).sorted()
        return games.mapIndexed { index, game -> (index+1) * game.bid }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
