package solvers.day16

import solvers.Solver
import solvers.day16.packets.LiteralValuePacket
import solvers.day16.packets.OperatorPacket
import solvers.day16.packets.Packet

class Day16Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val packet = parseHexadecimalString(input)

    private fun Packet<*>.versionSum(): Long {
        return when (this) {
            is LiteralValuePacket -> version.toLong()
            is OperatorPacket -> version.toLong() + body.sumOf { it.versionSum() }
            else -> 0
        }
    }

    override fun partOne(): Long {
        return packet.versionSum()
    }

    override fun partTwo(): Long {
        return packet.result.toLong()
    }
}