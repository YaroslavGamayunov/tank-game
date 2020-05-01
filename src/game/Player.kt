package game

import game.objects.GamePlayer
import java.io.Serializable
import kotlin.random.Random

class Player(name: String) : Serializable {
    /**
     * @param id Global player identifier, which is shared with other players
     */
    val id: String
    var name: String = name

    @Transient
    var localPlayerInstance: GamePlayer? = null

    init {
        id = (1..15)
                .map { Random(System.currentTimeMillis()).nextInt(0, 9) }
                .joinToString("")
    }
}