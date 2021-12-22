package solvers.day18

@JvmInline
value class NumberNode(val number: Int) : Node {
    override fun trySplit(): Node? = if (number >= 10) {
        PairNode(
            NumberNode(number / 2),
            NumberNode(number / 2 + number % 2)
        )
    } else null

    override fun tryAddingLeft(num: Int): Node = addNum(num)
    override fun tryAddingRight(num: Int): Node = addNum(num)

    private fun addNum(num: Int): NumberNode = NumberNode(number + num)

    override fun toString(): String {
        return number.toString()
    }

    override val magnitude
        get() = number.toLong()
}