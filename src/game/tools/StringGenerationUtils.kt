package game.tools

import kotlin.random.Random

fun numericString(length: Int, seed: Int): String {
    val charPool: List<Char> = ('0'..'9').toList()
    var random = Random(seed)
    return (1..length)
            .map { i -> random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("");
}