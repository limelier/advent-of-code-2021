package solvers.common

typealias IntArray2D = Array<IntArray>

fun IntArray2D(rows: Int, cols: Int): IntArray2D = Array(rows) { IntArray(cols) }

val IntArray2D.cols: Int
    get() = first().size

val IntArray2D.rows: Int
    get() = size

fun IntArray2D.get(point2i: Point2i) = this[point2i.row][point2i.col]
