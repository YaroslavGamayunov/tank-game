package game.objects

import game.tools.Vector2


class PlacementArea(objectID: Int, val ownerID : Int,
                    val lx : Int, val rx : Int, val ly : Int, val ry : Int
                    ) : GameObject(objectID), IGamePlayerProperty {
    override lateinit var owner : GamePlayer
    fun inside(pos : Vector2): Boolean {
        return (pos.x in lx..rx && pos.y in ly..ry)
    }

    override fun linkIdentifiers(iIdentityProvider: IIdentityProvider) {
        super.linkIdentifiers(iIdentityProvider)
        owner = iIdentityProvider.getObjectByID(ownerID) as GamePlayer
    }
}