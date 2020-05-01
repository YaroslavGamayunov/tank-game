package game

import game.objects.GamePlayer
import java.io.Serializable
import game.tools.generateNumericString

class Player(name: String) : Serializable {
    /**
     * @param id Global player identifier, which is shared with other players
     */
    val id: String = generateNumericString(15)

    var name: String = name

    var localPlayerInstance: GamePlayer? = null

}