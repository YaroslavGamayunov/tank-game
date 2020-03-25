package game

import java.text.FieldPosition

open class GameUnit(
    override val objectID: Int
                    ) : GameObject(objectID), IGamePlayerProperty, IGameLocated {

    final override var position : Vector2 = Vector2(0,0)
         protected set

    override fun linkIdentifiers(iIdentityProvider: IIdentityProvider) {
        super.linkIdentifiers(iIdentityProvider)
        owner = iIdentityProvider(playerID)
    }

    val playerID = owner.id;


}