package game.actions

import game.Game

open class GameAction : IGameAction {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onUnknownAction(this)
    }

    override fun invoke(game: Game) {
        game.actions.add(this)
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        return false;
    }

}