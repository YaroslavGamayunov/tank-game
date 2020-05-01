package game.tools

import kotlin.random.Random

fun generateNumericString(length: Int): String = (1..length)
        .map { Random(System.currentTimeMillis()).nextInt(0, 9) }
        .joinToString("")