package game

import java.io.Serializable
import java.lang.RuntimeException

class Game : Serializable, IIdentityProvider{
    val objects : ArrayList<GameObject> = arrayListOf()
    val actions : ArrayList<GameAction> = arrayListOf()
    var gameHasStarted = false

    override fun getObjectByID(objectID: Int): GameObject {
        for(x in objects){
            if(x.objectID == objectID){
                return x;
            }
        }

        throw RuntimeException("Can't find object with such ID" )
    }


}