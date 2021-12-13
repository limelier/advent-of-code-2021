package solvers

import common.Point2i

private val foldLineRegex = Regex("""fold along ([xy])=(\d+)""")

private enum class FoldType{ROW, COLUMN}
private data class Fold(val type: FoldType, val value: Int)

class Day13Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val dots: Set<Point2i>
    private val folds: List<Fold>

    init {
        val allLines = input.lines()
        val blankLineIndex = allLines.indexOf("")
        val pointLines = allLines.subList(0, blankLineIndex)
        val foldLines = allLines.subList(blankLineIndex + 1, allLines.size)

        dots = pointLines.map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            // convert x to column and y to row
            Point2i(y, x)
        }.toSet()

        folds = foldLines.map { line ->
            val (_, axisString, valueString) = foldLineRegex.matchEntire(line)!!.groupValues
            val foldType = if (axisString == "x") FoldType.COLUMN else FoldType.ROW
            val value = valueString.toInt()

            Fold(foldType, value)
        }
    }

    /** Fold the dots, removing overlaps */
    private fun Set<Point2i>.folded(fold: Fold): Set<Point2i> {
        val foldedDots = mutableSetOf<Point2i>()
        for ((row, col) in this) {
            val (type, value) = fold

            val foldedDot = if (type == FoldType.ROW) {
                val foldedRow = if (row > value) 2 * value - row else row
                Point2i(foldedRow, col)
            } else {
                val foldedCol = if (col > value) 2 * value - col else col
                Point2i(row, foldedCol)
            }

            foldedDots += foldedDot
        }

        return foldedDots
    }

    override fun partOne(): Long {
        return dots.folded(folds[0]).size.toLong()
    }

    override fun partTwo(): Long {
        var foldedDots = dots
        for (fold in folds) {
            foldedDots = foldedDots.folded(fold)
        }

        val maxRow = foldedDots.maxOf { it.row }
        val maxCol = foldedDots.maxOf { it.col }

        val bitmap = Array(maxRow + 1) { BooleanArray(maxCol + 1) { false } }
        for ((row, col) in foldedDots) {
            bitmap[row][col] = true
        }

        for (row in bitmap) {
            for (bit in row) {
                print(if (bit) '█' else ' ')
            }
            println()
        }

        print("The returned value before is the number of points in the output, see the printout for answer")
        return foldedDots.size.toLong()
    }
}