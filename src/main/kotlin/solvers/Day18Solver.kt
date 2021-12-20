package solvers

private interface Node {
    fun trySplit(): Node?

    fun tryAddingLeft(num: Int): Node?
    fun tryAddingRight(num: Int): Node?
}

@JvmInline
private value class NumberNode(val number: Int) : Node {
    override fun trySplit(): Node? = if (number >= 10) {
        PairNode(
            NumberNode(number / 2),
            NumberNode(number / 2 + number % 2)
        )
    } else null

    override fun tryAddingLeft(num: Int): Node = addNum(num)
    override fun tryAddingRight(num: Int): Node = addNum(num)

    private fun addNum(num: Int): NumberNode = NumberNode(number + num)
}

private class PairNode(val left: Node, val right: Node) : Node {
    companion object {
        private class Exploded(val leftNumber: Int?, val rightNumber: Int?, val remainder: Node?)
    }

    /** Repeatedly reduce, if possible, and return the result */
    fun reduced(): PairNode {
        var node: PairNode
        var reduced: PairNode? = this
        do {
            node = reduced!!
            reduced = node.tryReduceOnce() as PairNode
        } while (reduced != null)

        return node
    }

    /** Return the result reduced once, or `null` if reduction is impossible */
    private fun tryReduceOnce(): Node? {
        return tryExplode() ?: trySplit()
    }

    private fun tryExplode(): Node? = tryExplode(0)?.remainder

    private fun tryExplode(depth: Int): Exploded? {
        if (depth == 4) {
            // this pair is under 4 other pairs, and will explode
            val leftNumber = (left as NumberNode).number
            val rightNumber = (right as NumberNode).number
            val remainder = NumberNode(0)
            return Exploded(leftNumber, rightNumber, remainder)
        }

        // try exploding the child on the left
        if (left is PairNode) {
            val exploded = left.tryExplode(depth + 1)
            if (exploded != null) with(exploded) {
                val rightWithRightNumber = rightNumber?.let { right.tryAddingRight(it) }

                return if (rightWithRightNumber != null) {
                    // right number added to right member
                    Exploded(leftNumber, null, PairNode(NumberNode(0), rightWithRightNumber))
                } else {
                    // right number doesn't exist or could not be added to right member
                    Exploded(leftNumber, rightNumber, right)
                }
            }
        }

        // try exploding the child on the right
        if (right is PairNode) {
            val exploded = right.tryExplode(depth + 1)
            if (exploded != null) with(exploded) {
                val leftWithLeftNumber = leftNumber?.let { right.tryAddingLeft(it) }

                return if (leftWithLeftNumber != null) {
                    // left number added to left member
                    Exploded(null, rightNumber, PairNode(leftWithLeftNumber, NumberNode(0)))
                } else {
                    // left number doesn't exist or could not be added to left member
                    Exploded(leftNumber, rightNumber, right)
                }
            }
        }

        // can't explode either child
        return null
    }

    override fun trySplit(): Node? = left.trySplit() ?: right.trySplit()

    /** Try adding the number leftwards to the left member first, then the right member. If both fail, return `null` */
    override fun tryAddingLeft(num: Int): Node? {
        left.tryAddingLeft(num)?.let { return PairNode(it, right) }
        right.tryAddingLeft(num)?.let { return PairNode(left, it) }
        return null
    }

    /** Try adding the number rightwards to the right member first, then the left member. If both fail, return `null` */
    override fun tryAddingRight(num: Int): Node? {
        right.tryAddingRight(num)?.let { return PairNode(left, it) }
        left.tryAddingRight(num)?.let { return PairNode(it, right) }
        return null
    }

    operator fun plus(other: PairNode): PairNode = PairNode(this, other).reduced()
}

private val pairRegex = Regex("""\[(.*),(.*)]""")
private fun nodeFromInput(input: String): Node {
    val match = pairRegex.matchEntire(input)

    return if (match != null) {
        // the input is a pair
        val (_, left, right) = match.groupValues
        PairNode(nodeFromInput(left), nodeFromInput(right))
    } else {
        // the input is a number
        NumberNode(input.toInt())
    }
}

class Day18Solver(inputFilePath: String) : Solver(inputFilePath) {
    private val slugNumbers = input.lines().map { line -> nodeFromInput(line) as PairNode }

    override fun partOne(): Long {
        val sum = slugNumbers.reduce(PairNode::plus)

        TODO("return the magnitude of the sum")
    }

    override fun partTwo(): Long {
        TODO("Not yet implemented")
    }
}