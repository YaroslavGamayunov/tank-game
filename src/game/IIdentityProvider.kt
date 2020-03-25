package game

interface IIdentityProvider {
    fun getObjectByID(objectID : Int) : GameObject
}