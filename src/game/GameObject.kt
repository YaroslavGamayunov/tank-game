package game

import java.lang.RuntimeException

open class GameObject(open val objectID : Int) : IGameObject {
    override fun linkIdentifiers(iIdentityProvider: IIdentityProvider) {
        val myObject = iIdentityProvider.getObjectByID(objectID)
        if(myObject !== this){
            throw RuntimeException("Invalid IIdentityProvider: couldn't find me there or provided different instance")
        }
    }

    override fun cleanOnNewMove() {

    }
}