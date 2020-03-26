package game

open class GameUnit(
    override val objectID: Int,
             val playerID: Int,
                 position: Vector2

                    ) : GameObject(objectID), IGamePlayerProperty, IGameLocated {

    final override var position : Vector2 = position
         protected set

    @Transient
    override lateinit var owner : GamePlayer
         protected set

    override fun linkIdentifiers(iIdentityProvider: IIdentityProvider) {
        super.linkIdentifiers(iIdentityProvider)
        owner = iIdentityProvider.getObjectByID(playerID) as GamePlayer
    }



}