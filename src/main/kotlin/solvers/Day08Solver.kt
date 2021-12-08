package solvers


private data class Puzzle(
    val digitSignals: List<Set<Char>>,
    val outputSignals: List<Set<Char>>,
) {
    companion object {
        fun fromInputLine(inputLine: String): Puzzle {
            val (digitText, outputText) = inputLine.split("|").map { it.trim() }
            val digitSignals = digitText.split(" ").map { it.toSet() }
            val outputSignals = outputText.split(" ").map { it.toSet() }

            return Puzzle(digitSignals, outputSignals)
        }
    }
}

/*
 segments:
  aaaa
 b    c
 b    c
  dddd
 e    f
 e    f
  gggg
*/
private val digitSegments = listOf("abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg")
    .map { it.toSet() }

class Day08Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val puzzles = input.lines().map { Puzzle.fromInputLine(it) }

    /** Given a set of characters, and a pair of characters, find which one is in the set, and which isn't */
    private fun splitPair(set: Set<Char>, pair: Set<Char>): Pair<Char, Char> {
        val inside = pair.find { it in set }!!
        val outside = pair.find { it != inside }!!

        return inside to outside
    }

    /** Solve a puzzle, returning a string containing the four digits in the output */
    private fun solvePuzzle(puzzle: Puzzle): String {
        val (digitSignals, outputSignals) = puzzle
        val signalToSegment = mutableMapOf<Char, Char>()

        val allSignals = ('a'..'g').toSet()

        // 1, 4, 7, 8 have unique segment counts
        val oneSignals = digitSignals.find { it.size == digitSegments[1].size }!!
        val fourSignals = digitSignals.find { it.size == digitSegments[4].size }!!
        val sevenSignals = digitSignals.find { it.size == digitSegments[7].size }!!

        // 1 and 7 only differ by the 'a' segment
        val aSignal = (sevenSignals - oneSignals).first()
        signalToSegment[aSignal] = 'a'

        // 0, 6, 9 have 6 segments
        val zeroSixNineSignals = digitSignals.filter { it.size == 6 }
        /* 0, 6, 9 together contain the 'a', 'b', 'f', 'g' 3 times each, and 'c', 'd', 'e' 2 times each */
        val cdeSignals = allSignals
            .filter { signal -> zeroSixNineSignals.count { it.contains(signal) } == 2 }
            .toSet()

        // 1 contains 'c' and 'f'
        val (cSignal, fSignal) = splitPair(cdeSignals, oneSignals)
        signalToSegment[cSignal] = 'c'
        signalToSegment[fSignal] = 'f'

        // removing 1 from 4 leaves 'b' and 'd'
        val bdSignals = fourSignals - oneSignals
        val (dSignal, bSignal) = splitPair(cdeSignals, bdSignals)
        signalToSegment[bSignal] = 'b'
        signalToSegment[dSignal] = 'd'

        // the signals not in 4 that aren't 'a' are 'e' and 'g'
        val egSignals = allSignals - fourSignals - aSignal
        val (eSignal, gSignal) = splitPair(cdeSignals, egSignals)
        signalToSegment[eSignal] = 'e'
        signalToSegment[gSignal] = 'g'

        // we have all the mappings now, we can work on the output
        // find the actual segments lit up by the output signals
        val outputSegments = outputSignals.map { it.map { signal -> signalToSegment[signal]!! }.toSet() }
        // find which digit these segments represent
        val outputDigits = outputSegments.map { segments -> digitSegments.indexOfFirst { it == segments } }

        return outputDigits.joinToString("")
    }

    private val outputs = puzzles.map { solvePuzzle(it) }

    override fun partOne(): Long {
        // get total count of 1s, 4s, 7s and 8s in outputs
        return outputs.sumOf { output -> output.count { it in "1478" }.toLong() }
    }

    override fun partTwo(): Long {
        // get sum of output numbers
        return outputs.sumOf { it.toLong() }
    }
}