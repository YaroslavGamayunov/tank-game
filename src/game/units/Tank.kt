package game.units

import game.objects.IPositionProvider
import game.tools.*

open class Tank(objectID: Int, playerID: Int, position: Vector2, health: Int, orientation: Orientation,
                val damage : UInt, val moveDistance : Int) : GameUnit(objectID, playerID, position, health
)
{
    val maxShots = 1
    val maxTurns = 1

    var orientation : Orientation = orientation

    //This variables store temporary information during the turn
    var tempSteps : Int = 0
    var tempTurns : Int = 0
    var tempShots : Int = 0


    override fun cleanOnNewMove() {
        tempSteps = 0
        tempTurns = 0
        tempShots = 0
    }

    /**
     * @param position Caller must be sure that position is not equal to tank's position
     */
    open fun canShootPoint(position: Vector2, positionProvider: IPositionProvider) : Boolean{
        val dir = position - this.position
        if(dir.orientation!! != orientation)return false
        return positionProvider.isVacantHalfInterval(this.position + orientation.direction,
            position, orientation)
    }

    override fun toString(): String {
        return "{[Tank] id: $objectID owner: ${owner.toString()}}"
    }


}