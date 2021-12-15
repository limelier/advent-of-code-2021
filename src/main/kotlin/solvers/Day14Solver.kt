package solvers

/** Add `added` to `getValue(key)`, useful for maps with defaults */
private fun <K> MutableMap<K, Long>.addToValue(key: K, added: Long) {
    this[key] = this.getValue(key) + added
}

/** Get both the maxOf and the minOf at the same time */
private fun <K> Iterable<K>.minMaxOf(extractor: (K) -> Long): Pair<Long, Long> {
    var min: Long? = null
    var max: Long? = null

    for (element in this) {
        val result = extractor(element)
        if (min == null || result < min) min = result
        if (max == null || result > max) max = result
    }

    return min!! to max!!
}

/* A polymer will be interpreted as the number of each possible pair of adjacent characters in the polymer, as well
as the first and last character of it. The actual order of the characters in the polymer is not necessary */
class Day14Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val polymerTemplate: String
    private val insertionRules: Map<Pair<Char, Char>, Char>
    private val templatePairs: Map<Pair<Char, Char>, Long>
    private val possibleChars: Set<Char>
    private val ends: Pair<Char, Char>

    init {
        // extract the template
        val lines = input.lines()
        polymerTemplate = lines[0]

        // store the ends for later
        ends = polymerTemplate.first() to polymerTemplate.last()

        // create the first pair map
        val map = mutableMapOf<Pair<Char, Char>, Long>()::withDefault { 0 }
        for (i in 1 until polymerTemplate.length) {
            val pair = polymerTemplate[i - 1] to polymerTemplate[i]
            map.addToValue(pair, 1)
        }
        templatePairs = map

        // create the list of possible polymer characters
        possibleChars = polymerTemplate.toList().toMutableSet()

        // extract the rules
        val ruleLines = lines.subList(2, lines.size)
        insertionRules = ruleLines.associate { line ->
            val (input, output) = line.split(" -> ")
            (input[0] to input[1]) to output[0]
        }

        // complete the list of possible characters
        insertionRules.forEach { (pair, result) ->
            possibleChars += setOf(pair.first, pair.second, result)
        }
    }

    /** Apply the insertion rules to the given polymer */
    private fun Map<Pair<Char, Char>, Long>.applyRules(): Map<Pair<Char, Char>, Long> {
        val newMap = mutableMapOf<Pair<Char, Char>, Long>()::withDefault { 0 }

        for ((pair, num) in this) {
            val result = insertionRules.getValue(pair)

            val firstPair = pair.first to result
            val secondPair = result to pair.second

            newMap.addToValue(firstPair, num)
            newMap.addToValue(secondPair, num)
        }

        return newMap
    }

    /** Apply the insertion rules `steps` times and return the resulted polymer */
    private fun Map<Pair<Char, Char>, Long>.applyRules(steps: Int): Map<Pair<Char, Char>, Long> {
        var polymerPairs = this
        repeat(steps) {
            polymerPairs = polymerPairs.applyRules()
        }

        return polymerPairs
    }

    /**
     * Get the number of times a character appears in the polymer represented by the given pairs,
     * and ending with `ends`
     *
     * Total count: (appearances in all pairs + appearances at the ends) / 2
     */
    private fun Map<Pair<Char, Char>, Long>.getCharAppearances(c: Char): Long {
        var appearances = 0L

        for ((pair, num) in entries) {
            if (pair.first == c) appearances += num
            if (pair.second == c) appearances += num
        }

        if (ends.first == c) appearances++
        if (ends.second == c) appearances++

        return appearances / 2
    }

    /** Get the difference between the count of the char with the most appearances, and the char with the least */
    private fun getDifference(pairs: Map<Pair<Char, Char>, Long>): Long {
        val (min, max) = possibleChars.minMaxOf {
            pairs.getCharAppearances(it)
        }

        return max - min
    }


    private val pairsAfter10Steps = templatePairs.applyRules(10)
    private val pairsAfter40Steps = pairsAfter10Steps.applyRules(30)

    override fun partOne(): Long {
        return getDifference(pairsAfter10Steps)
    }

    override fun partTwo(): Long {
        return getDifference(pairsAfter40Steps)
    }
}