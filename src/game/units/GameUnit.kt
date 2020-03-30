package game.units

import game.controllers.GamePlayer
import game.objects.GameObject
import game.objects.IGameLocated
import game.objects.IGamePlayerProperty
import game.objects.IIdentityProvider
import game.tools.Vector2

open class GameUnit(
    override val objectID: Int,
    val playerID: Int,
    position: Vector2,
    health: Int
                    ) : GameObject(objectID), IGamePlayerProperty,
    IGameLocated {

    final override var position : Vector2 = position
//        protected set
    var health : Int = health
        protected set

    override val solid : Boolean = true

    @Transient
    override lateinit var owner : GamePlayer
         protected set

    override fun linkIdentifiers(iIdentityProvider: IIdentityProvider) {
        super.linkIdentifiers(iIdentityProvider)
        owner = iIdentityProvider.getObjectByID(playerID) as GamePlayer
    }

    open fun applyDamage(damage : UInt, attacker : GameUnit){
        health -= damage.toInt()
        if(health < 0) health = 0;
    }



}