package solvers.day04

internal class Cell(
    val value: Int
) {
    private var markedField: Boolean = false
    val marked get() = markedField

    fun checkAndMark(number: Int) {
        if (value == number) {
            markedField = true
        }
    }

    /** Render the number padded with spaces if not marked, or square brackets if marked */
    override fun toString(): String {
        val paddedNumber = value.toString().padStart(2, ' ')

        return if (marked) "[$paddedNumber]" else " $paddedNumber "
    }
}