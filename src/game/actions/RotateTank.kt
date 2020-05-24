package game.actions

import game.Game
import game.events.ActionEvent
import game.events.IGameEvent
import game.tools.Orientation

class RotateTank(val tankID: Int, val orientation: Orientation) : GameAction() {
    override fun invoke(visitor: IActionVisitor?) {
        visitor?.onTankTurned(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) : IGameEvent? {
        val tank = game.getTank(tankID)
        tank.assertProperty(game.currentMovePlayer)
        tank.assertCanRotate(orientation)
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            return ActionEvent {
                tank.orientation = orientation
                tank.tempTurns += 1
            }
        }
        return null
    }


}