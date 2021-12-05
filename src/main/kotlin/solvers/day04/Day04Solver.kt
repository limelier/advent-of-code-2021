package solvers.day04

import solvers.Solver

class Day04Solver : Solver("/inputs/04.txt"){
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

    override fun partOne(): Int {
        val firstWinningGame = finishedGames.minByOrNull { it.bingoNumberIndex!! }!!
        println(firstWinningGame)
        return firstWinningGame.getScore()!!
    }

    override fun partTwo(): Int {
        val lastWinningGame = finishedGames.maxByOrNull { it.bingoNumberIndex!! }!!
        println(lastWinningGame)
        return lastWinningGame.getScore()!!
    }
}