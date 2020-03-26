package game.actions

import game.Game
import game.GameAction
import game.GameActionSequence

class MoveEnd(val playerID: Int) : GameAction() {
    override fun invoke(game: Game) {
        super.invoke(game)
        game.waitingForPlayerToMove = false
    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveEnd(this)
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        return true
    }


}