package solvers.day18

import common.pairs
import solvers.Solver


class Day18Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val slugNumbers = input.lines().map { it.asNode() }

    override fun partOne(): Long {
        val sum = slugNumbers.reduce(Node::plus)
        println("Sum of all numbers: $sum")
        return(sum.magnitude)
    }

    override fun partTwo(): Long {
        return slugNumbers
            .pairs()
            .maxOf { (it.first + it.second).magnitude }
    }
}