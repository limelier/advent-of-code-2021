package solvers

abstract class Solver(
    val inputFilePath: String
) {
    /** The puzzle input, read from the text file at `inputFilePath` */
    protected val input = javaClass.getResource(inputFilePath)!!.readText()

    /** Calculate the answer to part one */
    abstract fun partOne(): Int

    /** Calculate the answer to part two */
    abstract fun partTwo(): Int
}