package solvers

import solvers.common.*

class Day11Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val initialState: IntArray2D = input.lines().map { line ->
        line.map { it.digitToInt() }.toIntArray()
    }.toTypedArray()

    private fun IntArray2D.ansiString(): String {
        return this.joinToString(System.lineSeparator()) { row ->
            row.joinToString("") {
                when (it) {
                    0 -> "${ANSI_BOLD}0${ANSI_RESET}"
                    in 1..9 -> "$it"
                    else -> "+"
                }
            }
        }
    }

    /** Execute a step on the octopus board, and return the next state paired with the number of flashes */
    private fun IntArray2D.next(): Pair<IntArray2D, Long> {
        val copy = this.deepClone()
        val maxPowerOctopi = ArrayDeque<Point2i>()

        // phase 1: increase power
        for (point in copy.coords()) {
            copy[point]++
            if (copy[point] > 9) {
                maxPowerOctopi.addFirst(point)
            }
        }

        // phase 2: flash
        var flashes = 0L
        while (maxPowerOctopi.isNotEmpty()) {
            val point = maxPowerOctopi.removeLast()
            // flash current
            copy[point] = 0
            flashes++

            // increase neighbors, add to deque if needed
            for (neighbor in point.eightNeighbors()) {
                if (!inBounds(neighbor)) continue
                if (copy[neighbor] == 0) continue  // this octopus has already flashed this step

                copy[neighbor]++
                if (copy[neighbor] > 9 && neighbor !in maxPowerOctopi) {
                    maxPowerOctopi.addFirst(neighbor)
                }
            }
        }

        return copy to flashes
    }

    override fun partOne(): Long {
        var state = initialState.deepClone()
        var flashes = 0L

        println("Before any steps: ")
        println(state.ansiString())
        println()

        for (step in 1..100) {
            val (nextState, stepFlashes) = state.next()
            state = nextState
            flashes += stepFlashes

            if (step < 10 || step % 10 == 0) {
                println("After step ${step}:")
                println(state.ansiString())
                println()
            }
        }

        return flashes
    }

    override fun partTwo(): Long {
        val boardSize = initialState.coords().size.toLong()

        var state = initialState.deepClone()
        var steps = 0L
        do {
            steps++
            val (nextState, stepFlashes) = state.next()
            state = nextState
        } while (stepFlashes != boardSize)

        println()
        println("After step $steps:")
        println(state.ansiString())
        println()

        return steps
    }
}