package ir.unalzo.utils

operator fun <T> List<T>.times(count: Int): List<T> {
    return List(count) {
        this
    }.flatten()
}

fun <T, R> List<T>.isAllSame(block: (T) -> R): Boolean {
    var result = mutableListOf<R>()
    forEach {
        result.add(block(it))
    }
    return result.toSet().count() == 1
}