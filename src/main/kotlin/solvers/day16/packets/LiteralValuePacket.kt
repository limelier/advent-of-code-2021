package solvers.day16.packets

import java.math.BigInteger

val LITERAL_VALUE_PACKET_TYPE_ID = 4.toUByte()

class LiteralValuePacket(
    version: UByte,
    body: BigInteger
) : Packet<BigInteger>(version, LITERAL_VALUE_PACKET_TYPE_ID, body) {
    override val result = body
}
