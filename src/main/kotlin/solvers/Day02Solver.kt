package solvers

class Day02Solver: Solver("/inputs/02.txt") {
    private enum class CommandType { FORWARD, DOWN, UP }
    private data class Command(val type: CommandType, val number: Int)
    private data class Position(val depth: Int, val horizontal: Int)

    private val commands = input.lines().map { line ->
        val components = line.split(" ")
        val type = when(components[0]) {
            "forward" -> CommandType.FORWARD
            "down" -> CommandType.DOWN
            "up" -> CommandType.UP
            else -> throw IllegalStateException("input file line '$line' is malformed")
        }
        val number = components[1].toInt()

        Command(type, number)
    }

    private fun followSimpleCommands(): Position {
        var pos = Position(0, 0)
        for ((type, number) in commands) pos = when(type) {
            CommandType.FORWARD -> Position(pos.depth, pos.horizontal + number)
            CommandType.DOWN -> Position(pos.depth + number, pos.horizontal)
            CommandType.UP -> Position(pos.depth - number, pos.horizontal)
        }
        return pos
    }

    private fun followComplexCommands(): Position {
        var pos = Position(0, 0)
        var aim = 0
        for ((type, number) in commands) when(type) {
            CommandType.FORWARD -> {
                pos = Position(pos.depth + aim * number, pos.horizontal + number)
            }
            CommandType.DOWN -> aim += number
            CommandType.UP -> aim -= number
        }
        return pos
    }

    override fun partOne(): Int {
        val position = followSimpleCommands()
        return position.depth * position.horizontal
    }

    override fun partTwo(): Int {
        val position = followComplexCommands()
        return position.depth * position.horizontal
    }
}