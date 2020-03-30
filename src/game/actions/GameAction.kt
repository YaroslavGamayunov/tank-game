package game.actions

import game.Game

open class GameAction : IGameAction {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onUnknownAction(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
        if(!checkCorrectnessOnly)game.addAction(this)
    }

}