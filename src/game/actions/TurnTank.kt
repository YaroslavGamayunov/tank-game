package game.actions

import game.Game
import game.tools.Orientation

class TurnTank(tankID: Int, val orientation: Orientation) : TankAction(tankID) {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onTankTurned(this)
    }

    override fun invoke(game: Game) {
        val tank = getTank(game)?:return
        tank.orientation = orientation
        tank.tempTurned = true
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        val tank = getTank(game)?:return false
        return !tank.tempTurned
    }

}