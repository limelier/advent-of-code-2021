package solvers.day04

import solvers.Solver

class Day04Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val calledNumbers: List<Int>
    private val games: List<BingoGame>

    init {
        val lines = input.lines()

        calledNumbers = lines[0].split(",").map { it.toInt() }

        games = lines.subList(1, lines.size).chunked(6) { chunk ->
            // each chunk will contain 6 lines: the newline before each board, and the board itself
            val bingoLines = chunk.subList(1, chunk.size)
            val bingoNumbers = bingoLines.map { line ->
                line
                    .split(" ")
                    .filter { it != "" }
                    .map { it.toInt() }
            }

            BingoGame(bingoNumbers, calledNumbers)
        }
    }

    private val finishedGames = games.filter { it.bingoNumberIndex != null }

    override fun partOne(): Long {
        val firstWinningGame = finishedGames.minByOrNull { it.bingoNumberIndex!! }!!
        println(firstWinningGame)
        return firstWinningGame.getScore()!!.toLong()
    }

    override fun partTwo(): Long {
        val lastWinningGame = finishedGames.maxByOrNull { it.bingoNumberIndex!! }!!
        println(lastWinningGame)
        return lastWinningGame.getScore()!!.toLong()
    }
}