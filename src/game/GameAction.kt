package game

import game.actions.IActionVisitor

open class GameAction : IGameAction{
    override fun invoke(visitor: IActionVisitor) {
        visitor.onUnknownAction(this)
    }

    override fun invoke(game: Game) {
        game.actions.add(this)
    }

    override fun isCorrect(game: Game): Boolean {
        return true;
    }

}