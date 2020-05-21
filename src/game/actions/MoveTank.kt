package game.actions

import game.*
import game.events.ActionEvent
import game.events.IGameEvent
import game.tools.Vector2

class MoveTank(val tankID : Int, val newPosition : Vector2) : GameAction(){
    override fun invoke(visitor: IActionVisitor?) {
        visitor?.onTankMove(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) : IGameEvent? {
        val tank = game.getTank(tankID)
        tank.assertProperty(game.currentMovePlayer)
        tank.assertMovingToAnotherCell(newPosition)
        val move = newPosition - tank.position
        val expectedOrientation = move.assertAndGetOrientation()
        tank.assertOrientation(expectedOrientation)
        tank.assertCanMoveThatFar(move)
        tank.assertMoveThroughEmptySpace(game, newPosition )
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            return ActionEvent {
                tank.tempSteps += (newPosition - tank.position).manhattanAbs
                tank.position = newPosition.copy()
            }
        }
        return null
    }
}