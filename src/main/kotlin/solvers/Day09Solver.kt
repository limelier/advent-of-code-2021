package solvers

import solvers.common.*

class Day09Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val heightMap: IntArray2D = input.lines().map { line ->
        line.toCharArray()
            .map { it.digitToInt() }
            .toIntArray()
    }.toTypedArray()

    private val lowPoints = heightMap.coords().filter { it.isLowPoint() }
    private val basinMap = IntArray2D(heightMap.rows, heightMap.cols) { -1 }
    private val basinSizes = IntArray(lowPoints.size) { 0 }

    init {
        // problem description states there are no "ridges" between basins that have a value other than 9
        // therefore it is safe to just flood-fill up to the 9's
        for ((index, lowPoint) in lowPoints.withIndex()) {
            floodFill(lowPoint, index)
        }
    }

    private fun Point2i.isLowPoint(): Boolean {
        return neighbors()
            .mapNotNull { heightMap.getOrNull(it) }
            .all { it > heightMap[this] }
    }

    /** Flood-fill a basin in the basinMap with basinIndex up to the 9-valued points on the edge */
    private fun floodFill(point: Point2i, basinIndex: Int) {
        basinMap[point] = basinIndex
        basinSizes[basinIndex]++
        for (neighbor in point.neighbors()) {
            if (!heightMap.inBounds(neighbor) || heightMap[neighbor] == 9 || basinMap[neighbor] != -1) continue
            floodFill(neighbor, basinIndex)
        }
    }

    override fun partOne(): Long {
        return lowPoints.sumOf { heightMap[it].toLong() + 1 }
    }

    override fun partTwo(): Long {
        return basinSizes
            .sortedDescending()
            .take(3)
            .map { it.toLong() }
            .reduce { acc, it -> acc * it }
    }
}