fun main() {
    class Range(val destinationRangeStart: Long, val sourceRangeStart: Long, range: Long) {
        val sourceRangeEnd: Long
        val offset: Long

        init {
            sourceRangeEnd = sourceRangeStart + range
            offset = destinationRangeStart - sourceRangeStart
        }

        fun isSourceValueInRange(sourceValue: Long): Boolean {
            return sourceValue in sourceRangeStart..<sourceRangeEnd
        }
    }

    fun toRange(it: String): Range {
        val line = it.split(" ")
        return Range(line[0].toLong(), line[1].toLong(), line[2].toLong())
    }

    fun performCalculation(input: List<String>, seeds: List<LongRange>): Long {
        val seedToSoilStart = input.indexOf("seed-to-soil map:") + 1
        val soilToFertilizerStart = input.indexOf("soil-to-fertilizer map:") + 1
        val fertilizerToWaterStart = input.indexOf("fertilizer-to-water map:") + 1
        val waterToLightStart = input.indexOf("water-to-light map:") + 1
        val lightToTemperatureStart = input.indexOf("light-to-temperature map:") + 1
        val temperatureToHumidityStart = input.indexOf("temperature-to-humidity map:") + 1
        val humidityToLocationStart = input.indexOf("humidity-to-location map:") + 1

        val seedToSoilMap = input.subList(seedToSoilStart, soilToFertilizerStart - 2)
        val soilToFertilizerMap = input.subList(soilToFertilizerStart, fertilizerToWaterStart - 2)
        val fertilizerToWaterMap = input.subList(fertilizerToWaterStart, waterToLightStart - 2)
        val waterToLightMap = input.subList(waterToLightStart, lightToTemperatureStart - 2)
        val lightToTemperatureMap = input.subList(lightToTemperatureStart, temperatureToHumidityStart - 2)
        val temperatureToHumidityMap = input.subList(temperatureToHumidityStart, humidityToLocationStart - 2)
        val humidityToLocationMap = input.subList(humidityToLocationStart, input.size)

        val seedToSoil = seedToSoilMap.map { toRange(it) }
        val soilToFertilizer = soilToFertilizerMap.map { toRange(it) }
        val fertilizerToWater = fertilizerToWaterMap.map { toRange(it) }
        val waterToLight = waterToLightMap.map { toRange(it) }
        val lightToTemperature = lightToTemperatureMap.map { toRange(it) }
        val temperatureToHumidity = temperatureToHumidityMap.map { toRange(it) }
        val humidityToLocation = humidityToLocationMap.map { toRange(it) }

        return seeds.minOf { seedRange ->
            seedRange.minOf {seed ->
                val soil = seed + (seedToSoil.find { range -> range.isSourceValueInRange(seed) }?.offset ?: 0)
                val fertilizer = soil + (soilToFertilizer.find { range -> range.isSourceValueInRange(soil) }?.offset ?: 0)
                val water = fertilizer + (fertilizerToWater.find { range -> range.isSourceValueInRange(fertilizer) }?.offset ?: 0)
                val light = water + (waterToLight.find { range -> range.isSourceValueInRange(water) }?.offset ?: 0)
                val temperature = light + (lightToTemperature.find { range -> range.isSourceValueInRange(light) }?.offset ?: 0)
                val humidity = temperature + (temperatureToHumidity.find { range -> range.isSourceValueInRange(temperature) }?.offset ?: 0)
                humidity + (humidityToLocation.find { range -> range.isSourceValueInRange(humidity) }?.offset ?: 0)
            }
        }
    }

    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter("seeds: ").trim().split(" ").map { it.toLong() }
        val seedRanges = seeds.map { seed -> LongRange(seed, seed) }

        return performCalculation(input, seedRanges)
    }

    fun part2(input: List<String>): Long {
        val numbers = input[0].substringAfter("seeds: ").trim().split(" ").map { it.toLong() }
        val seedRanges: List<LongRange> = numbers.windowed(2, 2).map {
            val start = it[0]
            val range = it[1]
            LongRange(start, start + range - 1)
        }

        return performCalculation(input, seedRanges)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
