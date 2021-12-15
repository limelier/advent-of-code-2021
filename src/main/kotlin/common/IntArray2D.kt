package common

fun Array<IntArray>.toIntArray2D(): IntArray2D = IntArray2D(this)

@JvmInline
value class IntArray2D(val rows: Array<IntArray>): Iterable<Int> {
    constructor(rows: Int, cols: Int) : this(Array(rows) { IntArray(cols) })
    constructor(rows: Int, cols: Int, function: () -> Int) : this(Array(rows) { IntArray(cols) { function() } })

    val numCols: Int
        get() = rows.first().size

    val numRows: Int
        get() = rows.size

    val size: Int
        get() = numCols * numRows

    // allow this[coord]
    operator fun get(coord: Point2i): Int = rows[coord.row][coord.col]
    operator fun set(coord: Point2i, value: Int) {
        rows[coord.row][coord.col] = value
    }

    // allow this[row, col]
    operator fun get(row: Int, col: Int): Int = rows[row][col]
    operator fun set(row: Int, col: Int, value: Int) {
        rows[row][col] = value
    }

    /** Check if a coordinate pair is in bounds for the 2D array */
    fun inBounds(coord: Point2i): Boolean = coord.row in 0 until numRows && coord.col in 0 until numCols

    /** Get the int at the coordinates if they are in bounds, or null otherwise */
    fun getOrNull(coord: Point2i): Int? = if (inBounds(coord)) get(coord) else null

    fun coords(): Sequence<Point2i> = sequence {
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                yield(Point2i(row, col))
            }
        }
    }

    fun deepClone(): IntArray2D = IntArray2D(rows.map { it.clone() }.toTypedArray())

    override fun iterator(): Iterator<Int> = sequence {
        for (row in rows) {
            for (element in row) {
                yield(element)
            }
        }
    }.iterator()
}