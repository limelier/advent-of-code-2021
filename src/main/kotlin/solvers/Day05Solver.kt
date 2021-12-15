package solvers

import common.IntArray2D
import common.Point2i

private data class Line(val start: Point2i, val end: Point2i) {
    fun isHorizontal(): Boolean = start.row == end.row
    fun isVertical(): Boolean = start.col == end.col

    val points: List<Point2i> get() = listOf(start, end)
}

private typealias OceanMap = IntArray2D

class Day05Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val lines = input.lines().map { line ->
        val (startString, endString) = line.split(" -> ")

        // flip coordinates, because the puzzle description apparently considers them col,row
        val (startCol, startRow) = startString.split(",").map { it.toInt() }
        val (endCol, endRow) = endString.split(",").map { it.toInt() }

        Line(Point2i(startRow, startCol), Point2i(endRow, endCol))
    }

    private val rowNum: Int
    private val colNum: Int

    init {
        val allPoints = lines.flatMap { it.points }
        rowNum = allPoints.maxOf { it.row } + 1
        colNum = allPoints.maxOf { it.col } + 1
    }

    /**
     * Pretty-print the ocean map
     *
     * The output can be quite big for actual puzzle inputs.
     * */
    private fun OceanMap.prettyPrint() {
        for (row in rows) {
            val rowString = row.joinToString("") {
                when (it) {
                    0 -> "."
                    in 1..9 -> "$it"
                    else -> "+"
                }
            }

            println(rowString)
        }
    }

    private fun OceanMap.drawHorizontal(row: Int, col1: Int, col2: Int) {
        val (startCol, endCol) = listOf(col1, col2).sorted()
        for (col in startCol..endCol) {
            this[row, col]++
        }
    }

    private fun OceanMap.drawVertical(col: Int, row1: Int, row2: Int) {
        val (startRow, endRow) = listOf(row1, row2).sorted()
        for (row in startRow..endRow) {
            this[row, col]++
        }
    }

    /** Draw a line at exactly 45 degrees, with one 'pixel' per row and column */
    private fun OceanMap.drawDiagonal(line: Line) {
        val rows = with(line) {
            if (start.row <= end.row) {
                start.row..end.row
            } else {
                start.row downTo end.row
            }
        }
        val cols = with(line) {
            if (start.col <= end.col) {
                start.col.. end.col
            } else {
                start.col downTo end.col
            }
        }

        for ((row, col) in rows.zip(cols)) {
            this[row, col]++
        }
    }

    /** Draw horizontal, vertical, or perfectly diagonal lines on the map */
    private fun OceanMap.drawLines(drawDiagonals: Boolean) {
        for (line in lines) {
            if (line.isHorizontal()) {
                drawHorizontal(line.start.row, line.start.col, line.end.col)
            } else if (line.isVertical()) {
                drawVertical(line.start.col, line.start.row, line.end.row)
            } else {
                if (!drawDiagonals) continue
                drawDiagonal(line)
            }
        }
    }

    /**
     * Get the number of points where there is an overlap in the lines
     *
     * Ignore diagonal lines if `drawDiagonals` is `false`
     */
    private fun getOverlaps(drawDiagonals: Boolean): Int {
        val oceanMap = OceanMap(rowNum, colNum)
        oceanMap.drawLines(drawDiagonals)

        if (oceanMap.numRows < 20 && oceanMap.numCols < 20) {
            // pretty print only for small maps
            oceanMap.prettyPrint()
            println()
        }

        return oceanMap.count { it >= 2 }
    }

    override fun partOne(): Long {
        return getOverlaps(drawDiagonals = false).toLong()
    }

    override fun partTwo(): Long {
        return getOverlaps(drawDiagonals = true).toLong()
    }
}

