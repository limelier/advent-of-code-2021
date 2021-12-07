import solvers.*
import solvers.day04.Day04Solver

/** Get a number from the input between 1 and `numDays` */
private fun chooseDay(numDays: Int): Int {
    print("Choose a day between 1 and $numDays inclusive: ")

    var day: Int? = null

    while (day == null) {
        day = readLine()!!.toIntOrNull()
        if (day !in 1 .. numDays) day = null
        if (day == null) print("Invalid day, try again: ")
    }

    return day
}

fun main() {
    val solverConstructors = listOf(
        ::Day01Solver,
        ::Day02Solver,
        ::Day03Solver,
        ::Day04Solver,
        ::Day05Solver,
        ::Day06Solver,
        ::Day07Solver,
    )
    val day = chooseDay(solverConstructors.size)
    val solver = solverConstructors[day - 1]()

    println("Solving day $day, for input at ${solver.inputFilePath}")

    try {
        println("Part one: ${solver.partOne()}")
        println("Part two: ${solver.partTwo()}")
    } catch (_: NotImplementedError) {
        // silently ignore, just stop printing when a part that isn't implemented is reached
    }
}