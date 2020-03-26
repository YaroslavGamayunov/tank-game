package game.actions

import game.Game
import game.objects.IIdentityProvider
import game.units.Tank

abstract class TankAction(val tankID : Int) : GameAction() {
    fun getTank(iIdentityProvider: IIdentityProvider) : Tank?{
        val obj = iIdentityProvider.getObjectByID(tankID)
        return if(obj is Tank) obj else null
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        return getTank(game) != null
    }
}