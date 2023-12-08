import java.math.BigInteger

class Node(val label: String, val leftLabel: String, val rightLabel: String) {
    lateinit var left: Node
    lateinit var right: Node
    fun isStart(): Boolean {
        return label == "AAA"
    }

    fun isEnd(): Boolean {
        return label == "ZZZ"
    }

    fun endsWithA(): Boolean {
        return label.endsWith("A")
    }

    fun endsWithZ(): Boolean {
        return label.endsWith("Z")
    }
}

data class Instructions(val directionSequence: String, val startNodes: List<Node>)

fun main() {
    fun parseInput(input: List<String>, part2: Boolean = false): Instructions {
        val directionSequence = input[0]

        val startNodes = mutableListOf<Node>()

        val nodes = input.subList(2, input.size).map {
            val label = it.substring(0, 3)
            val leftLabel = it.substring(7, 10)
            val rightLabel = it.substring(12, 15)
            val node = Node(label, leftLabel, rightLabel)
            if (part2) {
                if (node.endsWithA()) {
                    startNodes.add(node)
                }
            } else {
                if (node.isStart()) {
                    startNodes.add(node)
                }
            }
            node
        }.associateBy { it.label }

        nodes.values.forEach {
            it.left = nodes[it.leftLabel]!!
            it.right = nodes[it.rightLabel]!!
        }

        return Instructions(directionSequence, startNodes)
    }

    fun part1(input: List<String>): Int {
        val instructions = parseInput(input)

        var node = instructions.startNodes[0]
        var steps = 0
        while (!node.isEnd()) {
            val index = steps.mod(instructions.directionSequence.length)
            val direction = instructions.directionSequence[index]
            node = if (direction == 'R') {
                node.right
            } else {
                node.left
            }
            ++steps
        }

        return steps
    }


    fun lcm(numbers: List<Int>): BigInteger {
        return numbers
            .map { BigInteger.valueOf(it.toLong()) }
            .reduce{ acc, i ->
                acc * i / acc.gcd(i)
            }
    }

    fun part2(input: List<String>): BigInteger {
        val instructions = parseInput(input, true)
        val nodes = instructions.startNodes
        val stepsToFinalNode = nodes.map {
            var steps = 0
            var node = it
            while (!node.endsWithZ()) {
                val index = steps.mod(instructions.directionSequence.length)
                val direction = instructions.directionSequence[index]
                node = if (direction == 'R') {
                    node.right
                } else {
                    node.left
                }
                ++steps
            }
            steps
        }

        val lcm = lcm(stepsToFinalNode)
        stepsToFinalNode.forEach { println(lcm.divide(it.toBigInteger())) }
        return lcm
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day08_test1")
    check(part1(testInput1) == 2)

    val testInput2 = readInput("Day08_test2")
    check(part1(testInput2) == 6)

    val testInputPart2 = readInput("Day08_part2_test")
    check(part2(testInputPart2) == BigInteger.valueOf(6))

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
