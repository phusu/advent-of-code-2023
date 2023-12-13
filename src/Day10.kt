enum class Direction {
    NORTH,
    SOUTH,
    EAST,
    WEST
}

data class Coord(val x: Int, val y: Int)

class Tile(var char: Char, val coordinates: Coord) {

    fun isStartTile(): Boolean {
        return char == 'S'
    }

    fun getDirections(): List<Direction> {
        if (char == '|') {
            return listOf(Direction.NORTH, Direction.SOUTH)
        }
        if (char == '-') {
            return listOf(Direction.EAST, Direction.WEST)
        }
        if (char == 'L') {
            return listOf(Direction.NORTH, Direction.EAST)
        }
        if (char == 'J') {
            return listOf(Direction.NORTH, Direction.WEST)
        }
        if (char == '7') {
            return listOf(Direction.SOUTH, Direction.WEST)
        }
        if (char == 'F') {
            return listOf(Direction.SOUTH, Direction.EAST)
        }
        return listOf()
    }
}
fun main() {


    fun parseInput(input: List<String>): Pair<List<List<Tile>>, Coord> {
        var coordinates = Coord(0, 0)
        val tiles = input.mapIndexed { y, it ->
            it.asSequence()
                .mapIndexed { x, c ->
                    val tile = Tile(c, Coord(x, y))
                    if (tile.isStartTile()) {
                        coordinates = tile.coordinates
                    }
                    tile
                }
                .toList()
        }
        return Pair(tiles, coordinates)
    }

    fun isInTiles(coord: Coord, tiles: List<List<Tile>>): Boolean {
        return coord.x >= 0 && coord.y >= 0 && coord.x < tiles[0].size && coord.y < tiles.size
    }

    fun calculateStartTileDirections(tiles: List<List<Tile>>, c: Coord) {
        val directions = mutableListOf<Direction>()
        val possibleCoordinates = listOf(
            Coord(c.x+1, c.y),
            Coord(c.x, c.y+1),
            Coord(c.x-1, c.y),
            Coord(c.x, c.y-1),
            )

        if (isInTiles(possibleCoordinates[0], tiles) && tiles[possibleCoordinates[0].y][possibleCoordinates[0].x].getDirections().contains(Direction.WEST)) {
            directions.add(Direction.EAST)
        }
        if (isInTiles(possibleCoordinates[1], tiles) && tiles[possibleCoordinates[1].y][possibleCoordinates[1].x].getDirections().contains(Direction.NORTH)) {
            directions.add(Direction.SOUTH)
        }
        if (isInTiles(possibleCoordinates[2], tiles) && tiles[possibleCoordinates[2].y][possibleCoordinates[2].x].getDirections().contains(Direction.EAST)) {
            directions.add(Direction.WEST)
        }
        if (isInTiles(possibleCoordinates[3], tiles) && tiles[possibleCoordinates[3].y][possibleCoordinates[3].x].getDirections().contains(Direction.SOUTH)) {
            directions.add(Direction.NORTH)
        }

        var type = 'S'
        if (directions.contains(Direction.NORTH) && directions.contains(Direction.SOUTH)) {
            type = '|'
        } else if (directions.contains(Direction.EAST) && directions.contains(Direction.WEST)) {
            type = '-'
        } else if (directions.contains(Direction.NORTH) && directions.contains(Direction.EAST)) {
            type = 'L'
        } else if (directions.contains(Direction.NORTH) && directions.contains(Direction.WEST)) {
            type = 'J'
        } else if (directions.contains(Direction.SOUTH) && directions.contains(Direction.WEST)) {
            type = '7'
        } else if (directions.contains(Direction.SOUTH) && directions.contains(Direction.EAST)) {
            type = 'F'
        }
        tiles[c.y][c.x].char = type
    }

    fun neighbours(tiles: List<List<Tile>>, tile: Tile): Pair<Tile, Tile> {
        if (tile.char == '|') {
            return Pair(tiles[tile.coordinates.y-1][tile.coordinates.x], tiles[tile.coordinates.y+1][tile.coordinates.x])
        }
        if (tile.char == '-') {
            return Pair(tiles[tile.coordinates.y][tile.coordinates.x-1], tiles[tile.coordinates.y][tile.coordinates.x+1])
        }
        if (tile.char == 'L') {
            return Pair(tiles[tile.coordinates.y-1][tile.coordinates.x], tiles[tile.coordinates.y][tile.coordinates.x+1])
        }
        if (tile.char == 'J') {
            return Pair(tiles[tile.coordinates.y-1][tile.coordinates.x], tiles[tile.coordinates.y][tile.coordinates.x-1])
        }
        if (tile.char == '7') {
            return Pair(tiles[tile.coordinates.y+1][tile.coordinates.x], tiles[tile.coordinates.y][tile.coordinates.x-1])
        }
        if (tile.char == 'F') {
            return Pair(tiles[tile.coordinates.y+1][tile.coordinates.x], tiles[tile.coordinates.y][tile.coordinates.x+1])
        }
        throw IllegalStateException("No valid direction found")
    }

    fun part1(input: List<String>): Int {
        val parsedInput = parseInput(input)
        val tiles = parsedInput.first
        val startTileCoordinates = parsedInput.second
        val startTile = tiles[startTileCoordinates.y][startTileCoordinates.x]
        calculateStartTileDirections(tiles, startTileCoordinates)
        val visitedTiles = mutableListOf(startTile)
        var nextTile = neighbours(tiles, startTile).first

        while (nextTile != visitedTiles.first()) {
            val neighbours = neighbours(tiles, nextTile)
            val newNextTile = if (neighbours.first == visitedTiles.last()) {
                neighbours.second
            } else {
                neighbours.first
            }
            visitedTiles.add(nextTile)
            nextTile = newNextTile
        }

        return visitedTiles.size / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test1")
    check(part1(testInput) == 4)
    val testInput2 = readInput("Day10_test2")
    check(part1(testInput2) == 8)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
