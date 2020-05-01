package game

import game.objects.GamePlayer
import java.io.Serializable
import game.tools.numericString

class Player(name: String) : Serializable {
    /**
     * @param id Global player identifier, which is shared with other players
     */
    val id: String = numericString(15, System.currentTimeMillis().toInt())

    var name: String = name

    var localPlayerInstance: GamePlayer? = null

}