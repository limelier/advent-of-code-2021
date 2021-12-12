import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import solvers.solverConstructors
import kotlin.test.assertEquals

/** The pairs of answers for each day, where index X is for day X+1 */
private val sampleInputAnswers: List<Pair<Long, Long>> = listOf(
    7L to 5L,
    150L to 900L,
    198L to 230L,
    4512L to 1924L,
    5L to 12L,
    5934L to 26984457539L,
    37L to 168L,
    26L to 61229L,
    15L to 1134L,
    26397L to 288957L,
    1656L to 195L,
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