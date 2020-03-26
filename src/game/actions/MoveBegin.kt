package game.actions

import game.Game

class MoveBegin(val playerID : Int) : GameAction() {
    override fun invoke(game: Game) {
        super.invoke(game)
        game.currentMovePlayer = playerID
        game.waitingForPlayerToMove = true

        for(obj in game.objects){
            obj.cleanOnNewMove()
        }
    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onMoveStarted(this)
    }
}