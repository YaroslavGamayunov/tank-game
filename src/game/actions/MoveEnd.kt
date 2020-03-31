package game.actions

import game.Game
import game.events.ActionEvent
import game.events.IGameEvent

class MoveEnd(val playerID: Int) : GameAction() {

    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveEnd(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) : IGameEvent? {
        game.assertCanFinishMove()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            return ActionEvent {
                game.waitingForPlayerToMove = false;
            }
        }
        return null
    }



}