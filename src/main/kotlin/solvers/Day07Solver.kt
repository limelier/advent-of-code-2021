package solvers

import kotlin.math.abs


class Day07Solver : Solver("/inputs/07.txt") {
    private val positions = input.split(",").map { it.toInt() }
    private val min = positions.minOrNull()!!
    private val max = positions.maxOrNull()!!

    /** Calculate the number of moves required to get from one position to the other */
    private fun distance(pos1: Int, pos2: Int): Long {
        return abs(pos1 - pos2).toLong()
    }

    /** Returns the amount of fuel used by the actual crab submarines to go a certain distance */
    private fun fuelForDistance(distance: Long): Long {
        // Gaussian formula: 1+2+...+n = n*(n+1)/2
        return distance * (distance + 1) / 2
    }

    override fun partOne(): Long {
        return (min..max).minOf { finalPosition ->
            positions.sumOf { distance(finalPosition, it) }
        }
    }

    override fun partTwo(): Long {
        return (min..max).minOf { finalPosition ->
            positions.sumOf { fuelForDistance(distance(finalPosition, it)) }
        }
    }
}