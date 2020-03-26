package game.actions

import game.Game
import game.GameAction

class MoveBegin(val playerID : Int) : GameAction() {
    override fun invoke(game: Game) {
        super.invoke(game)
        game.currentMovePlayer = playerID
        game.waitingForPlayerToMove = true
    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveStarted(this)
    }
}