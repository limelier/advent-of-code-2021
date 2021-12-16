package solvers.day16

import solvers.day16.packets.LITERAL_VALUE_PACKET_TYPE_ID
import solvers.day16.packets.LiteralValuePacket
import solvers.day16.packets.OperatorPacket
import solvers.day16.packets.Packet

/** Parse a hexadecimal string encoding a packet */
fun parseHexadecimalString(hexString: String): Packet<*> {
    val binaryString = hexString
        .toCharArray()
        .joinToString("") { hexDigit ->
            val value = hexDigit.digitToInt(16).toString(2).padStart(4, '0')
            value
        }
    return parseBinaryString(binaryString, 1).first.single()
}

/**
 * Parse a binary string encoding a number of packets, and return it and the unused bits
 *
 * If `packetLimit` is not `null`, it represents how many packets should be parsed out of the string.
 */
private fun parseBinaryString(binaryString: String, packetLimit: Int? = null): Pair<List<Packet<*>>, String> {
    if (packetLimit == 0 || binaryString.length < 6) return emptyList<Packet<*>>() to binaryString

    val version = binaryString.substring(0..2).toUByte(2)
    val typeId = binaryString.substring(3..5).toUByte(2)
    val otherBits = binaryString.substring(6)

    val (leadingPacket, remainingBits) = when (typeId) {
        LITERAL_VALUE_PACKET_TYPE_ID -> parseLiteralValuePacket(version, otherBits)
        else -> parseOperatorPacket(version, typeId, otherBits)
    }

    val (otherPackets, unusedBits) = parseBinaryString(remainingBits, packetLimit?.let { it - 1 })
    return listOf(leadingPacket) + otherPackets to unusedBits
}

/** Parse the literal value packet with its body at the start of the given string, returning it and the remaining bits */
private fun parseLiteralValuePacket(version: UByte, bitString: String): Pair<LiteralValuePacket, String> {
    val nybbleChunks = mutableListOf<String>()
    for (chunk in bitString.chunkedSequence(5)) {
        val marker = chunk[0]
        nybbleChunks += chunk.substring(1)

        if (marker == '0') break // last chunk marker
    }
    val value = nybbleChunks.joinToString("").toBigInteger(2)
    val remainingBits = bitString.substring(nybbleChunks.size * 5)

    return LiteralValuePacket(version, value) to remainingBits
}

/** Parse the operator packet with its subpackets at the start of the given string, returning it and the remaining bits */
private fun parseOperatorPacket(version: UByte, typeId: UByte, bitString: String): Pair<OperatorPacket, String> {
    val lengthTypeId = bitString[0]

    val (subpackets, remainingBits) = if (lengthTypeId == '0') {
        // length is total length in bits
        val length = bitString.substring(1..15).toUInt(2).toInt()
        val subpacketBits = bitString.substring(16 until 16 + length)

        parseBinaryString(subpacketBits).first to bitString.substring(16 + length)
    } else {
        // length is number of subpackets
        val length = bitString.substring(1..11).toUInt(2).toInt()

        parseBinaryString(bitString.substring(12), length)
    }

    return OperatorPacket(version, typeId, subpackets) to remainingBits
}