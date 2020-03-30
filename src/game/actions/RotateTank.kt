package game.actions

import game.Game
import game.tools.Orientation

class RotateTank(val tankID: Int, val orientation: Orientation) : GameAction() {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onTankTurned(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
        val tank = game.getTank(tankID)
        tank.assertProperty(game.currentMovePlayer)
        tank.assertCanRotate(orientation)
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            tank.orientation = orientation
            tank.tempTurns += 1
        }
    }


}