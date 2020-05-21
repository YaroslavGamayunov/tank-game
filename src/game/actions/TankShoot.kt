package game.actions

import game.Game
import game.events.ComplexActionEvent
import game.events.IGameEvent

class TankShoot(val tankID: Int, val aimID: Int) : GameAction() {
    override fun invoke(visitor: IActionVisitor?) {
        visitor?.onTankShot(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean): IGameEvent? {
        val aim = game.getUnit(aimID)
        val tank = game.getTank(tankID)
        tank.assertHasShots()
        tank.assertProperty(game.currentMovePlayer)
        game.assertAttackAllowed(tank, aim)
        game.assertTankCanShootPosition(tank, aim.position)
        super.invoke(game, checkCorrectnessOnly)
        if (!checkCorrectnessOnly) {
            return ComplexActionEvent {
                tank.tempShots += 1
                game.applyDamage(aim, tank, tank.damage)
            }
        }
        return null
    }
}