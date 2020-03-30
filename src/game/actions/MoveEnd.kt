package game.actions

import game.Game

class MoveEnd(val playerID: Int) : GameAction() {

    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveEnd(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
        game.assertCanFinishMove()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            game.waitingForPlayerToMove = false;
        }
    }



}