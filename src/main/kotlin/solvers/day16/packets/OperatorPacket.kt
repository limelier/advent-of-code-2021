package solvers.day16.packets

import java.math.BigInteger

private val OPERATOR_SUM_TYPE_ID = 0.toUByte()
private val OPERATOR_PRODUCT_TYPE_ID = 1.toUByte()
private val OPERATOR_MIN_TYPE_ID = 2.toUByte()
private val OPERATOR_MAX_TYPE_ID = 3.toUByte()
private val OPERATOR_GT_TYPE_ID = 5.toUByte()
private val OPERATOR_LT_TYPE_ID = 6.toUByte()
private val OPERATOR_EQ_TYPE_ID = 7.toUByte()

private fun Boolean.toBigInteger(): BigInteger = if (this) BigInteger.valueOf(1) else BigInteger.valueOf(0)

class OperatorPacket(
    version: UByte,
    typeId: UByte,
    body: List<Packet<*>>
) : Packet<List<Packet<*>>>(version, typeId, body) {
    override val result: BigInteger
        get() = when (typeId) {
            OPERATOR_SUM_TYPE_ID -> body.sumOf { it.result }
            OPERATOR_PRODUCT_TYPE_ID -> body.map { it.result }.reduce { acc, el -> acc * el }
            OPERATOR_MIN_TYPE_ID -> body.minOf { it.result }
            OPERATOR_MAX_TYPE_ID -> body.maxOf { it.result }
            OPERATOR_GT_TYPE_ID -> (body[0].result > body[1].result).toBigInteger()
            OPERATOR_LT_TYPE_ID -> (body[0].result < body[1].result).toBigInteger()
            OPERATOR_EQ_TYPE_ID -> (body[0].result == body[1].result).toBigInteger()
            else -> throw IllegalStateException("unknown type ID for operator packet")
        }
}