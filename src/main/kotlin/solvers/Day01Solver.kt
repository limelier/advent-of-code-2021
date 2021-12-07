package solvers

/** Solver for [day 1](https://adventofcode.com/2021/day/1) **/
class Day01Solver : Solver("/inputs/01.txt") {
    private val values = input.lines().map { it.toInt() }

    /**
     * Calculate the number of times the current window's sum is larger than the last.
     */
    private fun countStepsUp(windowSize: Int = 1): Int {
        var stepsUp = 0
        // initialize previousWindowSum with the first window's sum
        var prevWindowSum = (0 until windowSize).sumOf { i -> values[i] }

        for (lastIdxBeforeWindow in 0 until values.size - windowSize) {
            // the windows only differ by one value
            val currWindowSum = prevWindowSum - values[lastIdxBeforeWindow] + values[lastIdxBeforeWindow + windowSize]
            if (currWindowSum > prevWindowSum) stepsUp++
            prevWindowSum = currWindowSum
        }
        return stepsUp
    }

    override fun partOne(): Long = countStepsUp().toLong()

    override fun partTwo(): Long = countStepsUp(3).toLong()
}