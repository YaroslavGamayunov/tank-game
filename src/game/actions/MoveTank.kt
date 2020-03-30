package game.actions

import game.*
import game.tools.Orientation
import game.tools.Vector2
import game.tools.isVacantHalfInterval
class MoveTank(val tankID : Int, val newPosition : Vector2) : GameAction(){
    override fun invoke(visitor: IActionVisitor) {
        visitor.onTankMove(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
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
            tank.tempSteps += (newPosition - tank.position).manhattanAbs
            tank.position = newPosition.copy()
        }
    }
}