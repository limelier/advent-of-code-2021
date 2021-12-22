package solvers.day18

sealed interface Node {
    fun trySplit(): Node?

    fun tryAddingLeft(num: Int): Node?
    fun tryAddingRight(num: Int): Node?

    val magnitude: Long

    operator fun plus(other: Node): PairNode = PairNode(this, other).reduced()
}

/** Create a node from an input string, in the same format as `toString` */
fun String.asNode(): Node {
    // the node might be a number node
    toIntOrNull()?.let { return NumberNode(it) }

    // the node is not a number; must be a pair

    val outerBracesStripped = substring(1, length - 1)
    var openBrackets = 0
    var commaIndex: Int? = null

    // find the index of the first comma not inside other brackets
    for ((idx, c) in outerBracesStripped.withIndex()) {
        if (c == ',' && openBrackets == 0) {
            commaIndex = idx
            break
        }

        if (c == '[') openBrackets++
        else if (c == ']') openBrackets--
    }

    if (commaIndex == null) {
        throw IllegalArgumentException("expected input '${this}' to be a pair, but no free comma was found")
    }

    val leftString = outerBracesStripped.substring(0, commaIndex)
    val rightString = outerBracesStripped.substring(commaIndex + 1)

    return PairNode(leftString.asNode(), rightString.asNode())
}
