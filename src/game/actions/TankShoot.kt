package game.actions

import game.Game

class TankShoot(val tankID: Int, val aimID : Int) : GameAction(){
    override fun invoke(visitor: IActionVisitor) {
        visitor.onTankShot(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
        val aim = game.getUnit(aimID)
        val tank = game.getTank(tankID)
        tank.assertHasShots()
        tank.assertProperty(game.currentMovePlayer)
        game.assertAttackAllowed(tank,aim)
        game.assertTankCanShootPosition(tank, aim.position)
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            game.applyDamage(aim,tank,tank.damage)
            tank.tempShots += 1
        }
    }
}