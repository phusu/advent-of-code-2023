fun main() {
    data class BoundingBox(val x1: Int, val y1: Int, val x2: Int, val y2: Int)
    data class PartNumber(val partNumber: String, val boundingBox: BoundingBox)
    data class Symbol(val s: String, val x: Int, val y: Int)
    data class Map(val partNumbers: List<PartNumber>, val symbols: List<Symbol>)

    fun parseData(input: List<String>): Map {
        val numberRegex = """\d+""".toRegex()
        val symbolRegex = """[^0-9.]""".toRegex()
        val partNumbers = ArrayList<PartNumber>()
        val symbols = ArrayList<Symbol>()

        input.forEachIndexed { index, line ->
            numberRegex.findAll(line).forEach {
                val numberStart = it.range.first()
                val numberEnd = it.range.last()
                val x1 = numberStart - 1
                val x2 = numberEnd + 1
                val y1 = index - 1
                val y2 = index + 1

                partNumbers.add(PartNumber(it.value, BoundingBox(x1, y1, x2, y2)))
            }
            symbolRegex.findAll(line).forEach {
                val x = it.range.first()
                val y = index

                symbols.add(Symbol(it.value, x, y))
            }
        }
        return Map(partNumbers, symbols)
    }

    fun isAdjacentToSymbol(partNumber: PartNumber, symbols: List<Symbol>): Boolean {
        val boundingBox = partNumber.boundingBox
        return symbols.any {
            it.x >= boundingBox.x1 && it.x <= boundingBox.x2 && it.y >= boundingBox.y1 && it.y <= boundingBox.y2
        }
    }

    fun calculateGearRatio(symbol: Symbol, partNumbers: List<PartNumber>): Int {
        if (symbol.s != "*") {
            return 0
        }
        val adjacentParts = partNumbers.filter {
            val boundingBox = it.boundingBox
            symbol.x >= boundingBox.x1 && symbol.x <= boundingBox.x2 && symbol.y >= boundingBox.y1 && symbol.y <= boundingBox.y2
        }
        if (adjacentParts.size != 2) {
            return 0
        }
        return adjacentParts[0].partNumber.toInt() * adjacentParts[1].partNumber.toInt()
    }

    fun part1(input: List<String>): Int {
        val map = parseData(input)

        return map.partNumbers
                .filter { isAdjacentToSymbol(it, map.symbols) }
                .sumOf { it.partNumber.toInt() }
    }

    fun part2(input: List<String>): Int {
        val map = parseData(input)
        return map.symbols
                .sumOf { calculateGearRatio(it, map.partNumbers) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
