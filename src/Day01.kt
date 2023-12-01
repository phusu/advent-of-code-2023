fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val noCharacters = it.replace(Regex("[^0-9]"), "")
            val firstNumber = noCharacters.first()
            val lastNumber = noCharacters.last()
            val combined = firstNumber + "" + lastNumber
            combined.toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val mapOfDigits = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9)
        return input.sumOf {
            var position = 0
            var convertedToDigits = it
            while (position < convertedToDigits.length) {
                mapOfDigits.forEach { (key, value) ->
                    if (convertedToDigits.indexOf(key) == position) {
                        convertedToDigits = convertedToDigits.replaceRange(position + 1, position + 2, value.toString())
                    }
                }
                ++position
            }
            val noCharacters = convertedToDigits.replace(Regex("[^0-9]"), "")

            val firstNumber = noCharacters.first()
            val lastNumber = noCharacters.last()
            val combined = firstNumber + "" + lastNumber
            combined.toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day01_test")
    val testInput2 = readInput("Day01_test2")
    check(part1(testInput1) == 142)
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
