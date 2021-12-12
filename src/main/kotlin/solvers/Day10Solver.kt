package solvers

import solvers.common.*
import java.util.*

private val illegalCharScore = mapOf(')' to 3L, ']' to 57L, '}' to 1197L, '>' to 25137L)
private val missingCharScore = mapOf(')' to 1L, ']' to 2L, '}' to 3L, '>' to 4L)
private val openToClose = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

private enum class CheckState {
    OK,             // payload will be null
    CORRUPTED,      // payload will be the first illegal character
    INCOMPLETE,     // payload will be the characters required to close the chunk
}
private data class CheckResult (val state: CheckState, val payload: String?)

class Day10Solver(inputFilePath: String) : Solver(inputFilePath) {
    /** Highlight the first character in a corrupted line using ANSI escape codes */
    private fun highlightCorruption(line: String, badIndex: Int): String {
        val goodBit = line.substring(0 until badIndex)
        val badChar = line[badIndex]
        val ignoredBit = line.substring(badIndex + 1 until line.length)

        return "$goodBit$ANSI_BOLD_RED$badChar$ANSI_DARK_GREY$ignoredBit$ANSI_RESET"
    }

    /** Highlight the completion for a line using ANSI escape codes */
    private fun highlightIncomplete(line: String, extra: String): String {
        return "$line$ANSI_YELLOW$extra$ANSI_RESET"
    }

    /**
     * Analyze a line, determining if it is corrupted, incomplete, or okay
     *
     * Corrupted lines result in a payload of the first invalid character.
     * Incomplete lines result in a payload of what would be necessary to complete them.
     */
    private fun analyzeLine(line: String): CheckResult {
        val stack = ArrayDeque<Char>()

        for ((index, char) in line.withIndex()) {
            if (char in "([{<") {
                stack.push(char)
            } else {
                val correct = openToClose[stack.pop()]
                if (char != correct) {
                    println(highlightCorruption(line, index))
                    return CheckResult(CheckState.CORRUPTED, char.toString())
                }
            }
        }

        return if (stack.isEmpty()) {
            println(line)
            CheckResult(CheckState.OK, null)
        } else {
            val missing = stack.map { openToClose[it] }.joinToString("")
            println(highlightIncomplete(line, missing))
            CheckResult(CheckState.INCOMPLETE, missing)
        }
    }

    private val results = input.lines().map { analyzeLine(it) }

    override fun partOne(): Long {
        return results
            .filter { it.state == CheckState.CORRUPTED }
            .sumOf { illegalCharScore[it.payload!![0]]!! }
    }

    override fun partTwo(): Long {
        // get a sorted score list
        val scores = results
            .filter { it.state == CheckState.INCOMPLETE }
            .map {
                var sum = 0L
                for (char in it.payload!!) {
                    sum = sum * 5 + missingCharScore[char]!!
                }
                sum
            }
            .sorted()

        // return the middle score
        return scores[(scores.size - 1) / 2]
    }
}