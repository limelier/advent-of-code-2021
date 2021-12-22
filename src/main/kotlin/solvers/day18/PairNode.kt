package solvers.day18

class PairNode(val left: Node, val right: Node) : Node {
    companion object {
        private class Exploded(val leftNumber: Int?, val rightNumber: Int?, val remainder: Node)
    }

    /** Repeatedly reduce, if possible, and return the result */
    fun reduced(): PairNode {
        var node: PairNode
        var reduced: Node? = this
        do {
            node = reduced!! as PairNode
            reduced = node.tryReduceOnce()
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

        // try an explosion inside the child on the left
        if (left is PairNode) {
            val exploded = left.tryExplode(depth + 1)
            if (exploded != null) with(exploded) {
                val rightWithRightNumber = rightNumber?.let { right.tryAddingLeft(it) }

                return if (rightWithRightNumber != null) {
                    // right number added to right member
                    Exploded(leftNumber, null, PairNode(remainder, rightWithRightNumber))
                } else {
                    // right number doesn't exist or could not be added to right member
                    Exploded(leftNumber, rightNumber, PairNode(remainder, right))
                }
            }
        }

        // try an explosion inside the child on the right
        if (right is PairNode) {
            val exploded = right.tryExplode(depth + 1)
            if (exploded != null) with(exploded) {
                val leftWithLeftNumber = leftNumber?.let { left.tryAddingRight(it) }

                return if (leftWithLeftNumber != null) {
                    // left number added to left member
                    Exploded(null, rightNumber, PairNode(leftWithLeftNumber, remainder))
                } else {
                    // left number doesn't exist or could not be added to left member
                    Exploded(leftNumber, rightNumber, PairNode(left, remainder))
                }
            }
        }

        // can't explode either child
        return null
    }

    override fun trySplit(): Node? {
        left.trySplit()?.let { return PairNode(it, right) }
        right.trySplit()?.let { return PairNode(left, it) }
        return null
    }

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

    override val magnitude: Long
        get() = 3 * left.magnitude + 2 * right.magnitude

    override fun toString(): String {
        return "[${left},${right}]"
    }
}