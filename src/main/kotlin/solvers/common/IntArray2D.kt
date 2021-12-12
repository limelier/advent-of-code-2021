package solvers.common

typealias IntArray2D = Array<IntArray>

fun IntArray2D(rows: Int, cols: Int): IntArray2D = Array(rows) { IntArray(cols) }
@Suppress("FunctionName") // masquerade as constructor
fun IntArray2D(rows: Int, cols: Int, function: () -> Int) = Array(rows) { IntArray(cols) { function() } }

val IntArray2D.cols: Int
    get() = first().size

val IntArray2D.rows: Int
    get() = size

operator fun IntArray2D.get(coord: Point2i): Int = this[coord.row][coord.col]
operator fun IntArray2D.set(coord: Point2i, value: Int) {
    this[coord.row][coord.col] = value
}

/** Check if a coordinates are in bounds for the 2D array */
fun IntArray2D.inBounds(coord: Point2i) = coord.row in 0 until rows && coord.col in 0 until cols

/** Get the int at the coordinates if in bounds, or null otherwise */
fun IntArray2D.getOrNull(coord: Point2i): Int? = if (inBounds(coord)) get(coord) else null

/** Get a list of all valid coordinates */
fun IntArray2D.coords() = (0 until rows).flatMap { row ->
    (0 until cols).map { col ->
        Point2i(row, col)
    }
}

fun IntArray2D.deepClone(): IntArray2D = this.map { it.clone() }.toTypedArray()