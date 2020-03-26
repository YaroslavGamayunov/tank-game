package game.actions

import java.io.Serializable

class GameActionSequence(val playerID: Int) : Serializable {
    val actions : ArrayList<IGameAction> = arrayListOf()
}