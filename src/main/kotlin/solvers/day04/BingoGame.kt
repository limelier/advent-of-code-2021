package solvers.day04

/** A Bingo game, with the board and all called numbers */
internal class BingoGame(
    valueLines: List<List<Int>>,
    private val calledNumbers: List<Int>
) {
    private val lines = valueLines.map { valueLine ->
        valueLine.map { Cell(it) }
    }

    val bingoNumberIndex = fillIn()

    /**
     * Fill the board in with the given numbers, returning the index of the last called number that gave bingo, or null
     * if no bingo ever occurs
     */
    private fun fillIn(): Int? {
        for ((index, number) in calledNumbers.withIndex()) {
            lines.onEach { line -> line.onEach { it.checkAndMark(number) } }

            if (hasBingo()) return index
        }
        return null
    }

    /** Check if a bingo has occurred */
    private fun hasBingo(): Boolean {
        // if any line is complete, return true
        if (lines.any { line -> line.all { it.marked }}) return true

        // if any column is complete, return true
        for (columnIndex in lines.indices) {
            if (lines.all { line -> line[columnIndex].marked }) return true
        }

        return false
    }

    /** Get the score of the board, as the sum of unmarked numbers times the last number marked */
    fun getScore(): Int? {
        if (bingoNumberIndex == null) return null

        val sum = lines.sumOf { line ->
            line.filter { !it.marked }.sumOf { it.value }
        }
        val lastNumber = calledNumbers[bingoNumberIndex]

        return sum * lastNumber
    }

    override fun toString(): String {
        val boardLines = lines.joinToString(System.lineSeparator()) { line ->
            line.joinToString(separator = "", transform = Cell::toString)
        }

        val bingoLine = if (bingoNumberIndex != null) {
            "bingo at number with index $bingoNumberIndex (${calledNumbers[bingoNumberIndex]}); score is ${getScore()}"
        } else {
            "bingo not obtained"
        }

        return """
LINES:
$boardLines

BINGO:
$bingoLine
        """
    }
}