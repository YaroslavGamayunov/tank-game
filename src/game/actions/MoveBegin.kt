package game.actions

import game.Game

class MoveBegin(val playerID : Int) : GameAction() {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveStarted(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
        game.assertCanStartNewMove()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly) {
            game.currentMovePlayer = playerID
            game.waitingForPlayerToMove = true
            game.cleanObjectsTemp()
        }
    }
}