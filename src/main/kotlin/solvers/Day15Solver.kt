package solvers

import common.*
import java.util.*
import kotlin.math.abs

private enum class PreviousNeighbor {
    UP,
    DOWN,
    LEFT,
    RIGHT,
}

private fun Point2i.fourNeighborsWithPrev(): List<Pair<Point2i, PreviousNeighbor>> = listOf(
    this.up() to PreviousNeighbor.DOWN,
    this.down() to PreviousNeighbor.UP,
    this.left() to PreviousNeighbor.RIGHT,
    this.right() to PreviousNeighbor.LEFT,
)

class Day15Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val cave: IntArray2D = input.lines().map { line ->
        line.map { it.digitToInt() }.toIntArray()
    }
        .toTypedArray()
        .toIntArray2D()
    private val start = Point2i(0, 0)

    /** Return the Manhattan distance between the two points */
    private fun manhattanDistanceBetween(first: Point2i, second: Point2i): Int {
        return abs(first.row - second.row) + abs(first.col - second.col)
    }

    private fun reconstructPath(previous: Array<Array<PreviousNeighbor?>>, goal: Point2i): List<Point2i> {
        var current = goal
        val path = mutableListOf(current)

        var prev = previous[current.row][current.col]
        while (prev != null) {
            current = when(prev) {
                PreviousNeighbor.UP -> current.up()
                PreviousNeighbor.DOWN -> current.down()
                PreviousNeighbor.LEFT -> current.left()
                PreviousNeighbor.RIGHT -> current.right()
            }
            path.add(0, current)
            prev = previous[current.row][current.col]
        }

        if (path[0] != start) throw IllegalStateException("optimal path to goal does not start at start")
        return path
    }

    private fun aStar(
        cost: (Point2i) -> Int = { cave[it] },
        caveNumRows: Int = cave.numRows,
        goal: Point2i = Point2i(cave.numRows - 1, cave.numCols - 1)
    ): List<Point2i> {
        /** The cost of the cheapest known path to get to this position from start */
        val g = IntArray2D(caveNumRows, caveNumRows) { Int.MAX_VALUE }
        g[start] = 0

        /** The current best guess for the cost to get from a position to the end */
        val f = IntArray2D(caveNumRows, caveNumRows) { Int.MAX_VALUE }
        f[start] = manhattanDistanceBetween(start, goal)

        /** Positions that have been reached, but not fully explored */
        val openPositions = PriorityQueue<Point2i>(compareBy { f[it] })
        openPositions.add(start)

        /** The previous neighbor in the optimal path to this position, for every position */
        val previous = Array(caveNumRows) { Array<PreviousNeighbor?>(caveNumRows) { null } }

        while (openPositions.isNotEmpty()) {
            val current = openPositions.poll()
            if (current == goal) return reconstructPath(previous, goal)

            for ((neighbor, prev) in current.fourNeighborsWithPrev()) {
                if (!g.inBounds(neighbor)) continue
                val tentativeG = g[current] + cost(neighbor)
                if (tentativeG < g[neighbor]) {
                    previous[neighbor.row][neighbor.col] = prev
                    g[neighbor] = tentativeG
                    f[neighbor] = tentativeG + manhattanDistanceBetween(neighbor, goal)
                    if (neighbor !in openPositions) {
                        openPositions.add(neighbor)
                    }
                }
            }
        }

        throw IllegalStateException("no optimal path was found")
    }

    private fun extendedCost(point: Point2i): Int {
        val posInTile = Point2i(
            point.row % cave.numRows,
            point.col % cave.numCols,
        )

        val gridTileCoord = Point2i(
            point.row / cave.numRows,
            point.col / cave.numCols,
        )

        val bigCost = cave[posInTile] + (gridTileCoord.row + gridTileCoord.col)
        return (bigCost - 1) % 9 + 1
    }

    private fun pathCost(path: List<Point2i>): Long = path.subList(1, path.size).sumOf { extendedCost(it).toLong() }

    /** Print path to console, using ANSI escape sequences for highlighting */
    private fun printPath(path: List<Point2i>, cost: (Point2i) -> Int, rows: Int, cols: Int) {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val point = Point2i(row, col)
                if (point in path) {
                    print("$ANSI_BOLD${cost(point)}$ANSI_RESET")
                } else {
                    print("$ANSI_DARK_GREY${cost(point)}$ANSI_RESET")
                }
            }
            println()
        }
    }

    override fun partOne(): Long {
        val path = aStar()
        printPath(path, { cave[it] }, cave.numRows, cave.numCols)
        return pathCost(path)
    }

    override fun partTwo(): Long {
        val extendedNumRows = 5 * cave.numRows
        val path = aStar(
            cost = ::extendedCost,
            caveNumRows = extendedNumRows,
            goal = Point2i(extendedNumRows - 1, extendedNumRows - 1)
        )

        if (extendedNumRows <= 100) printPath(path, ::extendedCost, extendedNumRows, extendedNumRows)

        return pathCost(path)
    }
}