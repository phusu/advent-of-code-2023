fun main() {
    data class Card(val id: Int, val winningNumbers: List<Int>, val numbersYouHave: List<Int>)

    fun parseInput(input: List<String>): List<Card> {
        return input.map {
            val id = it.substringAfter("Card ").substringBefore(":").trim().toInt()
            val allCards = it.substringAfter(':').split("|")
            val winningNumbers = allCards[0].trim()
                .split("""\s+""".toRegex())
                .map { s -> s.toInt() }
            val numbersYouHave = allCards[1].trim()
                .split("""\s+""".toRegex())
                .map { s -> s.toInt() }
            Card(id, winningNumbers, numbersYouHave)
        }
    }

    fun power(baseVal: Int, exponentVal: Int): Int {
        var exponent = exponentVal
        var result = 1

        while (exponent != 0) {
            result *= baseVal
            --exponent
        }
        return result
    }


    fun calculateWinningNumbersInHand(card: Card) = card.winningNumbers.intersect(card.numbersYouHave.toSet()).size

    fun calculatePoints(card: Card): Int {
        val winningNumbersInHand = calculateWinningNumbersInHand(card)
        return if (winningNumbersInHand == 0) {
            0
        } else {
            power(2, winningNumbersInHand - 1)
        }
    }

    fun part1(input: List<String>): Int {
        val cards = parseInput(input)
        return cards.sumOf { calculatePoints(it) }
    }

    fun part2(input: List<String>): Int {
        val cards = parseInput(input).map { mutableListOf(it) }
        cards.forEachIndexed { index, cardList ->
            cardList.forEach { card ->
                val winningNumbersInHand = calculateWinningNumbersInHand(card)
                for (i in index+1..index+winningNumbersInHand) {
                    cards[i].add(cards[i][0])
                }
            }
        }

        return cards.sumOf { it.size }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
