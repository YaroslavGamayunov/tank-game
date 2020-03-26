package game.actions

import game.*
import game.tools.Vector2

class MoveTank(tankID : Int, val newPosition : Vector2) :TankAction(tankID) {
    override fun invoke(game: Game) {
        super.invoke(game)
        val tank = getTank(game)?:return
        tank.tempSteps += (newPosition - tank.position).manhattanAbs
        tank.position = newPosition.copy()

    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onTankMove(this)
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        val tank  = getTank(game) ?: return false
        val move = newPosition - tank.position
        val concreteOrientation = (newPosition - tank.position).orientation
        if(concreteOrientation == null || concreteOrientation != tank.orientation) return false
        if(tank.tempSteps + move.manhattanAbs > tank.moveDistance)return false
        return true
    }
}