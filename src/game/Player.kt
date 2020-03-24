package game

import java.io.Serializable
import kotlin.random.Random

class Player(name: String) : Serializable {
    val id: String
    var name: String = name

    init {
        id = (1..15)
            .map { Random.nextInt(0, 9) }
            .joinToString("")
    }
}