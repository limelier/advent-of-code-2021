package solvers.day16.packets

import java.math.BigInteger

abstract class Packet<T>(
    val version: UByte,
    val typeId: UByte,
    val body: T,
) {
    abstract val result: BigInteger
}