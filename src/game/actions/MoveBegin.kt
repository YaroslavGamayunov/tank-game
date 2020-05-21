package game.actions

import game.Game
import game.events.ActionEvent
import game.events.IGameEvent

class MoveBegin(val playerID : Int) : GameAction() {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveStarted(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) : IGameEvent? {
        game.assertCanStartNewMove()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly) {
            return ActionEvent {
                game.currentMovePlayer = playerID
                game.waitingForPlayerToMove = true
                game.cleanObjectsTemp()
            }
        }
        return null
    }
}