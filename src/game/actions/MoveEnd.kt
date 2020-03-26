package game.actions

import game.Game
import game.GameAction

class MoveEnd(val playerID: Int) : GameAction() {
    override fun invoke(game: Game) {
        super.invoke(game)
        game.waitingForPlayerToMove = false
    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveEnd(this)
    }


}