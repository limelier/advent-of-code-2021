package common

data class Point2i(
    val row: Int,
    val col: Int,
) {
    fun up() = Point2i(row - 1, col)
    fun down() = Point2i(row + 1, col)
    fun left() = Point2i(row, col - 1)
    fun right() = Point2i(row, col + 1)
    fun fourNeighbors() = listOf(up(), down(), left(), right())
    fun eightNeighbors() = fourNeighbors() + listOf(up().left(), up().right(), down().left(), down().right())
}
