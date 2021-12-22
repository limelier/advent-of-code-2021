import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import solvers.solverConstructors
import kotlin.test.assertEquals

/** The pairs of answers for each day, where index X is for day X+1 */
private val sampleInputAnswers: List<Pair<Long, Long>> = listOf(
    7L to 5L,                   // day 1
    150L to 900L,               // day 2
    198L to 230L,               // day 3
    4512L to 1924L,             // day 4
    5L to 12L,                  // day 5
    5934L to 26984457539L,      // day 6
    37L to 168L,                // day 7
    26L to 61229L,              // day 8
    15L to 1134L,               // day 9
    26397L to 288957L,          // day 10
    1656L to 195L,              // day 11
    10L to 36L,                 // day 12
    17L to 16L,                 // day 13
    1588L to 2188189693529L,    // day 14
    40L to 315L,                // day 15
    31L to 54L,                 // day 16
    45L to 112L,                // day 17
    4140L to 3993L,                // day 18
)

/** Test if each solver gets the right answers for its respective day */
class SolverTest {
    companion object {
        // required to supply the days to the parameterized test
        // see: https://stackoverflow.com/a/70062963
        @JvmStatic
        val days by lazy { 1..sampleInputAnswers.size }
    }

    @ParameterizedTest
    @MethodSource("getDays")
    fun completesDaySample(day: Int) {
        val solverConstructor = solverConstructors[day - 1]
        val (partOneAnswer, partTwoAnswer) = sampleInputAnswers[day - 1]
        val inputFilePath = "/sample_inputs/${day.toString().padStart(2, '0')}.txt"

        val solver = solverConstructor(inputFilePath)
        assertEquals(partOneAnswer, solver.partOne(), "part one answer was wrong")
        assertEquals(partTwoAnswer, solver.partTwo(), "part two answer was wrong")
    }
}