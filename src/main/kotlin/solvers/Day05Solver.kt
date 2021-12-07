package solvers

private data class Point(val row: Int, val col: Int)
private data class Line(val start: Point, val end: Point) {
    fun isHorizontal(): Boolean = start.row == end.row
    fun isVertical(): Boolean = start.col == end.col

    val points: List<Point> get() = listOf(start, end)
}
private typealias OceanMap = Array<IntArray>

class Day05Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val lines = input.lines().map { line ->
        val (startString, endString) = line.split(" -> ")

        // flip coordinates, because the puzzle description apparently considers them col,row
        val (startCol, startRow) = startString.split(",").map { it.toInt() }
        val (endCol, endRow) = endString.split(",").map { it.toInt() }

        Line(Point(startRow, startCol), Point(endRow, endCol))
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
    @Suppress("unused")
    private fun OceanMap.prettyPrint() {
        for (row in this) {
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
            this[row][col]++
        }
    }

    private fun OceanMap.drawVertical(col: Int, row1: Int, row2: Int) {
        val (startRow, endRow) = listOf(row1, row2).sorted()
        for (row in startRow..endRow) {
            this[row][col]++
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
            this[row][col]++
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



    private fun getOverlaps(drawDiagonals: Boolean): Int {
        val oceanMap = Array(rowNum) { IntArray(colNum) { 0 } }
        oceanMap.drawLines(drawDiagonals)

        // uncomment only for inputs that would produce a small map (less than 20x20)
//        oceanMap.prettyPrint()

        return oceanMap.sumOf { row -> row.count { it >= 2 } }
    }

    override fun partOne(): Long {
        return getOverlaps(drawDiagonals = false).toLong()
    }

    override fun partTwo(): Long {
        return getOverlaps(drawDiagonals = true).toLong()
    }
}