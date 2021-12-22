package solvers

import common.BooleanArray2D
import common.Point2i
import common.toBooleanArray2D

private fun String.toBooleanArray(): BooleanArray = this.map { it == '#' }.toBooleanArray()
private fun BooleanArray.toInt(): Int {
    var num = 0
    for (bit in this) {
        num = num shl 1
        if (bit) num++
    }
    return num
}

/**
 * The infinite image is represented as an [inside] section, which is non-uniform and contains `.` and `#`, and an
 * [outside] one, which is uniform and is either all `.` or all `#`
 */
private class InfiniteImage(val inside: BooleanArray2D, var outside: Boolean) {
    constructor(nonUniformRows: Int, nonUniformCols: Int, outside: Boolean)
            : this(BooleanArray2D(nonUniformRows, nonUniformCols), outside)

    operator fun get(coord: Point2i): Boolean = if (inside.inBounds(coord)) inside[coord] else outside
    operator fun set(coord: Point2i, value: Boolean) {
        if (inside.inBounds(coord)) {
            inside[coord] = value
        } else {
            throw IllegalArgumentException("tried to set point at coords in uniform 'outside' area")
        }
    }

    fun getAlgorithmInput(coord: Point2i): BooleanArray {
        val up = coord.up()
        val down = coord.down()

        return listOf(up.left(), up, up.right(), coord.left(), coord, coord.right(), down.left(), down, down.right())
            .map { this[it] }
            .toBooleanArray()
    }

    fun applyAlgorithm(algorithm: BooleanArray): InfiniteImage {
        val next = InfiniteImage(inside.numRows + 2, inside.numCols + 2, !outside)

        for (row in -1..inside.numRows) {
            for (col in -1..inside.numCols) {
                val coord = Point2i(row, col)
                val algorithmInput = this.getAlgorithmInput(coord)
                val newPoint = algorithm[algorithmInput.toInt()]
                // 'next' infinite image is shifted by 1 down and to the right to reset coordinates
                next[coord.down().right()] = newPoint
            }
        }

        return next
    }

    val litPixels: Int get() = if (outside) throw IllegalStateException("infinite lit pixels") else inside.count { it }
}

class Day20Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val algorithm: BooleanArray
    private val inputImage: InfiniteImage

    init {
        val lines = input.lines()

        algorithm = lines[0].toBooleanArray()

        val inside = lines.subList(2, lines.size)
            .map { it.toBooleanArray() }
            .toTypedArray()
            .toBooleanArray2D()
        inputImage = InfiniteImage(inside, false)
    }

    override fun partOne(): Long {
        return inputImage
            .applyAlgorithm(algorithm)
            .applyAlgorithm(algorithm)
            .litPixels.toLong()
    }

    override fun partTwo(): Long {
        var image = inputImage
        for (step in 1..50) {
            image = image.applyAlgorithm(algorithm)
        }
        return image.litPixels.toLong()
    }
}