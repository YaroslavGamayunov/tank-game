package game.events

import game.objects.IIdentityProvider
import game.units.GameUnit

class UnitDestroyed(val unit : GameUnit, val iIdentityProvider: IIdentityProvider) : GameEvent() {
    override fun invoke(visitor: IEventVisitor) {
        visitor.onUnitDestroyed(this)
    }

    override fun invoke(): IGameEvent? {
        iIdentityProvider.removeObjcetByID(unit.objectID)
        return null
    }
}