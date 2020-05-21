package game.objects

import game.objects.GameObject

interface IIdentityProvider {
    fun getObjectByID(objectID : Int) : GameObject
    fun removeObjcetByID(objectID: Int)
    fun cleanObjectsTemp()
    fun vacantID() : Int
}