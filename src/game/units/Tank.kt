package game.units

import game.tools.Orientation
import game.tools.Vector2

open class Tank(objectID: Int, playerID: Int, position: Vector2, health: Int, orientation: Orientation,
                val damage : Int, val moveDistance : Int) : GameUnit(objectID, playerID, position, health
)
{
    var orientation : Orientation = orientation
        set

    //This variables store temporary information during the turn
    var tempSteps : Int = 0
    var tempTurned : Boolean = false
    var tempShot : Boolean = false


    override fun cleanOnNewMove() {
        tempSteps = 0
        tempTurned = false
        tempShot = false
    }
}