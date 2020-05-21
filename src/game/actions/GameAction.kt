package game.actions

import game.Game
import game.events.IGameEvent

open class GameAction : IGameAction, java.io.Serializable {
    override fun invoke(visitor: IActionVisitor?) {
        visitor?.onUnknownAction(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean): IGameEvent? {
        if (!checkCorrectnessOnly) game.addAction(this)
        return null
    }

}