package game

import game.actions.IActionList
import game.actions.IGameAction
import game.actions.IllegalActionException
import game.actions.WrongIdException
import game.objects.*
import game.tools.Vector2
import game.tools.copySerializable
import java.io.Serializable
import java.lang.RuntimeException

class Game : Serializable, IIdentityProvider, IPositionProvider, IActionList {

    //Data of game state
    val objects : ArrayList<GameObject> = arrayListOf()
    val actions : ArrayList<IGameAction> = arrayListOf()
    var gameHasStarted = false
    var gameIsOver = false
    var currentMovePlayer = -1
    var waitingForPlayerToMove = false
    val fieldSize = Vector2(50,50)
    var friendlyFireAllowed = true

    //Necessary methods
    override fun getObjectByID(objectID: Int): GameObject {
        for(x in objects){
            if(x.objectID == objectID){
                return x;
            }
        }
        throw WrongIdException(objectID)
    }

    override fun removeObjcetByID(objectID: Int) {
        objects.remove(getObjectByID(objectID))
    }

    override fun cleanObjectsTemp() {
        for(obj in objects){
            obj.cleanOnNewMove()
        }
    }

    override fun vacantID(): Int = (objects.maxBy { it.objectID }?.objectID?:0) + 1



    override fun getObjects(position: Vector2): List<IGameLocated> {
        val result = mutableListOf<IGameLocated>()
        for(x in objects){
            if(x is IGameLocated){
                if(x.position == position)result.add(x)
            }
        }
        return result
    }

    override fun getSolid(position: Vector2): IGameLocated? {
        return getObjects(position).firstOrNull { x -> x.solid }
    }

    override fun isValidCell(position: Vector2): Boolean {
        return position.x >= 0 && position.x < fieldSize.x && position.y >= 0 && position.y < fieldSize.y
    }

    override fun addAction(action: IGameAction) {
        actions.add(action)
    }

    fun copy() : Game{
        val game = copySerializable() as Game

        for(x in game.objects){
            x.linkIdentifiers(game)
        }
        return game
    }

}