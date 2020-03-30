package game.actions

import java.io.Serializable

class GameActionSequence(val playerID: Int) : Serializable, IActionList {
    val actions : ArrayList<IGameAction> = arrayListOf()
    override fun addAction(action: IGameAction) {
        actions.add(action)
    }
}