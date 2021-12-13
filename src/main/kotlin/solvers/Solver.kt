package solvers

import solvers.day04.Day04Solver

/** The constructor at index X instantiates the solver for day X+1 */
val solverConstructors = listOf(
    ::Day01Solver,
    ::Day02Solver,
    ::Day03Solver,
    ::Day04Solver,
    ::Day05Solver,
    ::Day06Solver,
    ::Day07Solver,
    ::Day08Solver,
    ::Day09Solver,
    ::Day10Solver,
    ::Day11Solver,
    ::Day12Solver,
)

abstract class Solver(
    inputFilePath: String
) {
    /** The puzzle input, read from the text file at `inputFilePath` */
     protected val input = javaClass.getResource(inputFilePath)!!.readText()

    /** Calculate the answer to part one */
    abstract fun partOne(): Long

    /** Calculate the answer to part two */
    abstract fun partTwo(): Long
}