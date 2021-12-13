package solvers

private fun String.isUppercase() = this.all { it.isUpperCase() }

private class Cave(
    val name: String
) {
    val isLarge = name.isUppercase()
    val neighbors = mutableListOf<Cave>()

    override fun toString(): String {
        return "($name: ${neighbors.joinToString { it.name }})"
    }
}

private class CaveSystem {
    val caves = mutableListOf<Cave>()

    private fun getOrCreate(name: String): Cave {
        return caves.firstOrNull() { it.name == name }
            ?: Cave(name).also { caves += it }
    }

    /** Add a connection to the cave system, creating the caves as needed */
    fun addConnection(firstName: String, secondName: String) {
        val first = getOrCreate(firstName)
        val second = getOrCreate(secondName)

        if (first.isLarge && second.isLarge) {
            println("Warning: direct connection between two large caves ($first and $second) may lead to infinite loop")
        }

        first.neighbors += second
        second.neighbors += first
    }

    override fun toString(): String {
        return caves.toString()
    }

    /**
     * Find all possible paths through a cave system, from 'start' to 'end'
     *
     * Small (lowercase-keyed) nodes can only be visited once, while large notes can be visited many times
     * If `canVisitOneSmallCaveTwice` is true, then one small cave can be visited twice instead of once
     */
    private fun findPaths(visited: List<Cave>, canVisitOneSmallCaveTwice: Boolean): List<List<Cave>> {
        val lastCave = visited.last()
        if (lastCave.name == "end") return listOf(visited)

        val validNeighbors = lastCave.neighbors.filter { cave ->
            when {
                cave.isLarge -> true
                cave.name == "start" -> false
                cave.name == "end" -> true
                else -> if (canVisitOneSmallCaveTwice) {
                    visited.count { it == cave } < 2
                } else {
                    cave !in visited
                }
            }
        }

        if (validNeighbors.isEmpty()) return listOf()
        return validNeighbors.flatMap { neighbor ->
            val canStillVisitOneSmallCaveTwice = canVisitOneSmallCaveTwice && (
                neighbor.isLarge || // large caves don't affect canVisitOneSmallCaveTwice
                neighbor !in visited // this small cave hasn't been visited yet
            )

            findPaths(visited + neighbor, canStillVisitOneSmallCaveTwice)
        }
    }

    fun findPaths(canVisitOneSmallCaveTwice: Boolean = false): List<List<Cave>> {
        return findPaths(listOf(getOrCreate("start")), canVisitOneSmallCaveTwice)
    }
}

class Day12Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val caveSystem = CaveSystem()

    init {
        for (line in input.lines()) {
            val (first, second) = line.split("-")
            caveSystem.addConnection(first, second)
        }
        println(caveSystem)
    }

    override fun partOne(): Long {
        val paths = caveSystem.findPaths()
        // uncomment to get printout in the same order as samples
//            .sortedBy { caves -> caves.joinToString("") { it.name } }

        println()
        paths.forEach { caves -> println(caves.joinToString { it.name }) }
        return paths.count().toLong()
    }

    override fun partTwo(): Long {
        val paths = caveSystem.findPaths(canVisitOneSmallCaveTwice = true)
        // uncomment to get printout in the same order as samples
//            .sortedBy { caves -> caves.joinToString("") { it.name } }

        println()
        paths.forEach { caves -> println(caves.joinToString { it.name }) }
        return paths.count().toLong()
    }
}