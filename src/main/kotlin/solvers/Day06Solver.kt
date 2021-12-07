package solvers

class Population (
    /** Separate lanternfish, represented as numbers equal to their timer value */
    lanternfish: List<Int>
) {
    /** Each bucket holds the number of lanternfish with the timer value equal to the index of the bucket */
    private val buckets = LongArray(9)

    init {
        for (fishTimerValue in lanternfish) {
            buckets[fishTimerValue]++
        }
    }

    val totalCount get() = buckets.sum()

    /**
     * Advance the population by one day:
     * - tick the timer down for each fish
     * - if a fish was already at 0, tick it up to 6 and add a fish with timer 8
     */
    fun next(): Population {
        val newPopulation = Population(listOf())

        // tick down the timer for most fish
        for (i in 0..7) {
            newPopulation.buckets[i] = buckets[i + 1]
        }

        // tick over fish with timer 0
        newPopulation.buckets[6] += buckets[0]

        // add new fish with timer 8
        newPopulation.buckets[8] += buckets[0]

        return newPopulation
    }

    /** Advance the population by multiple days */
    fun advance(days: Int): Population {
        var pop = this
        for (day in 1..days) {
            pop = pop.next()
        }
        return pop
    }

    override fun toString(): String {
        val populationString = buckets
            .mapIndexed { idx, count -> "$idx: $count" }
            .joinToString("; ")

        return "Population($populationString; total count: $totalCount)"
    }
}

class Day06Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val initialPopulation = Population(input.split(",").map { it.toInt() })

    init {
        println("Initial population: $initialPopulation")
    }

    private val populationAfter80Days = initialPopulation.advance(80)
    private val populationAfter256Days = populationAfter80Days.advance(256 - 80)

    override fun partOne(): Long {
        println("Population after 80 days: $populationAfter80Days")
        return populationAfter80Days.totalCount
    }

    override fun partTwo(): Long {
        println("Population after 256 days: $populationAfter256Days")
        return populationAfter256Days.totalCount
    }
}