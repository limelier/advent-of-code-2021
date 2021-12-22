package common

fun <T> Iterable<T>.pairs(pairWithSelf: Boolean = false): Sequence<Pair<T, T>> {
    val values = this

    return sequence {
        for ((i, first) in values.withIndex()) {
            for ((j, second) in values.withIndex()) {
                if (i == j && !pairWithSelf) continue
                yield(first to second)
            }
        }
    }
}