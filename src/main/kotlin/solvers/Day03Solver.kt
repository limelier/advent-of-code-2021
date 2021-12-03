package solvers

/**
 * Solver for [day 3](https://adventofcode.com/2021/day/3)
 *
 * This class uses 0/1 bits and false/true booleans interchangeably.
 */
class Day03Solver : Solver("/inputs/03.txt"){
    private data class BitCounter(var zeroes: Int = 0, var ones: Int = 0) {
        /** Increment the right counter of the character is a '0' or '1', or throw an IllegalStateException otherwise */
        fun increment(bit: Char) = when(bit) {
            '0' -> zeroes++
            '1' -> ones++
            else -> throw IllegalStateException("bit string had character '$bit' when '0' or '1' expected")
        }
    }

    private val bitStrings = input.lines()
    private val bitsPerLine = bitStrings[0].length

    /** Invert a '0' into a '1', a '1' into a '0', or throw an exception otherwise */
    private fun Char.invert(): Char = when(this) {
        '0' -> '1'
        '1' -> '0'
        else -> throw IllegalArgumentException("attempted to invert '$this' instead of '0' or '1'")
    }

    /** Invert a string of zeroes and ones, turning ones to zeroes and zeroes to ones. */
    private fun String.invert(): String = this.toList().map { it.invert() }.joinToString("")


    /** Get power consumption, `gamma * epsilon`, from the diagnostic report */
    private fun getPowerConsumption(): Int {
        val counters = List(bitsPerLine) { BitCounter() }

        for (bitString in bitStrings) {
            for ((idx, bit) in bitString.withIndex()) {
                counters[idx].increment(bit)
            }
        }

        val gammaBits = counters
            .map { if (it.ones > it.zeroes) '1' else '0' }
            .joinToString(separator = "")
        val gamma = gammaBits.toInt(radix = 2)
        val epsilon = gammaBits.invert().toInt(radix = 2)
        return gamma * epsilon
    }

    /**
     * Get a rating as described in part 2
     *
     * If `keepMostCommon` is `true`, return the oxygen generator rating. Otherwise, return the CO2 scrubber rating.
     */
    private fun getRating(keepMostCommon: Boolean): Int {
        var filteredBitStrings = bitStrings
        var bitIndex = 0
        while (bitIndex < bitsPerLine) {
            if (filteredBitStrings.count() == 1) break

            val counter = BitCounter()
            for (bitString in filteredBitStrings) counter.increment(bitString[bitIndex])

            // if one of the counters is zero, that means all strings share the same bit on the given position
            // in that case, skip filtering
            if (counter.ones != 0 && counter.zeroes != 0) {
                val mostCommon = if (counter.ones >= counter.zeroes) '1' else '0'
                val filter = if (keepMostCommon) mostCommon else mostCommon.invert()

                filteredBitStrings = filteredBitStrings.filter { it[bitIndex] == filter }
            }

            bitIndex++
        }

        if (filteredBitStrings.count() > 1) {
            throw IllegalStateException("there are multiple bit strings left after filtering")
        }

        return filteredBitStrings[0].toInt(radix = 2)
    }

    /**
     * Get life support rating from the diagnostic report
     *
     * This rating is the product of the oxygen generator rating and the CO2 scrubber rating
     */
    private fun getLifeSupportRating(): Int {
        val oxygenGeneratorRating = getRating(keepMostCommon = true)
        val co2ScrubberRating = getRating(keepMostCommon = false)

        return oxygenGeneratorRating * co2ScrubberRating
    }

    override fun partOne(): Int {
        return getPowerConsumption()
    }

    override fun partTwo(): Int {
        return getLifeSupportRating()
    }
}
