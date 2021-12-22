package common

fun Array<BooleanArray>.toBooleanArray2D(): BooleanArray2D = BooleanArray2D(this)

// yes, this is just the IntArray2D class again
// i'm not sure how to extract common code just yet

@JvmInline
value class BooleanArray2D(val rows: Array<BooleanArray>): Iterable<Boolean> {
    constructor(rows: Int, cols: Int) : this(Array(rows) { BooleanArray(cols) })
    constructor(rows: Int, cols: Int, function: () -> Boolean) : this(Array(rows) { BooleanArray(cols) { function() } })

    val numCols: Int
        get() = rows.first().size

    val numRows: Int
        get() = rows.size

    val size: Int
        get() = numCols * numRows

    // allow this[coord]
    operator fun get(coord: Point2i): Boolean = rows[coord.row][coord.col]
    operator fun set(coord: Point2i, value: Boolean) {
        rows[coord.row][coord.col] = value
    }

    // allow this[row, col]
    operator fun get(row: Int, col: Int): Boolean = rows[row][col]
    operator fun set(row: Int, col: Int, value: Boolean) {
        rows[row][col] = value
    }

    /** Check if a coordinate pair is in bounds for the 2D array */
    fun inBounds(coord: Point2i): Boolean = coord.row in 0 until numRows && coord.col in 0 until numCols

    /** Get the int at the coordinates if they are in bounds, or null otherwise */
    fun getOrNull(coord: Point2i): Boolean? = if (inBounds(coord)) get(coord) else null

    fun coords(): Sequence<Point2i> = sequence {
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                yield(Point2i(row, col))
            }
        }
    }

    fun deepClone(): BooleanArray2D = BooleanArray2D(rows.map { it.clone() }.toTypedArray())

    override fun iterator(): Iterator<Boolean> = sequence {
        for (row in rows) {
            for (element in row) {
                yield(element)
            }
        }
    }.iterator()
}