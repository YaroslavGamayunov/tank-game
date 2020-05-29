package game.actions

import game.Game
import game.events.ActionEvent
import game.events.IGameEvent
import game.objects.GamePlayer
import game.objects.PlacementSet
import game.tools.Orientation
import game.tools.Vector2
import game.units.tanks.Tank

class TankPlacement(val setID : Int, val pos: Vector2, var dir: Orientation) : GameAction() {
    override fun invoke(visitor: IActionVisitor?) {
        visitor?.onTankPlaced(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean): IGameEvent? {
        super.invoke(game, checkCorrectnessOnly)
        val set = game.getObjectByID(setID) as PlacementSet
        game.assertCanPlace(pos, game.getObjectByID(game.currentMovePlayer) as GamePlayer)
        set.assertProperty(game.currentMovePlayer)
        game.assertPlacementStage()
        game.assertSetNotEmpty(set)

        if(!checkCorrectnessOnly){
            return ActionEvent{
                set.place(game, pos, dir)
                if(!game.isThereTanksToPlaceLeft()){
                    game.isTankPlacementStage = false
                }
            }
        }
        return null
    }
}

fun Game.assertSetNotEmpty(set: PlacementSet) {
    if(set.count <= 0) throw PlacementException("No tanks of this type left")
}

fun Game.isThereTanksToPlaceLeft() : Boolean{
    val sets = objects.filterIsInstance<PlacementSet>()
    for (x in sets){
        if(x.count > 0 )return true
    }
    return false
}
