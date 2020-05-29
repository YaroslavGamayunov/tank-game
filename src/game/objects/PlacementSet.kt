package game.objects

import game.Game
import game.tools.Orientation
import game.tools.Vector2
import game.units.tanks.Tank

open class PlacementSet(objectID: Int, val ownerID : Int, var count : Int, val uniqueChar: Char) : GameObject(objectID), IGamePlayerProperty {
    override lateinit var owner: GamePlayer
    open fun place(game : Game, position : Vector2, dir : Orientation){
        --count
    }

    override fun linkIdentifiers(iIdentityProvider: IIdentityProvider) {
        super.linkIdentifiers(iIdentityProvider)
        owner = iIdentityProvider.getObjectByID(ownerID) as GamePlayer
    }
}

class HeavyPlacementSet(objectID: Int, ownerID: Int, count: Int, uniqueChar: Char) : PlacementSet(objectID, ownerID, count,
    uniqueChar
){
    override fun place(game: Game, position: Vector2, dir: Orientation) {
        super.place(game, position, dir)
        val tank = Tank(game.vacantID(), ownerID, position, 8, dir, 4U, 2)
        tank.name = "Heavy Tank"
        game.objects.add(tank)
        tank.linkIdentifiers(game)
    }
}

class LightPlacementSet(objectID: Int, ownerID: Int, count: Int, uniqueChar: Char) : PlacementSet(objectID, ownerID, count,
    uniqueChar
){
    override fun place(game: Game, position: Vector2, dir: Orientation) {
        super.place(game, position, dir)
        val tank = Tank(game.vacantID(), ownerID, position, 4, dir, 4U, 4)
        tank.name = "Light Tank"
        game.objects.add(tank)
        tank.linkIdentifiers(game)
    }
}

class MiddlePlacementSet(objectID: Int, ownerID: Int, count: Int, uniqueChar: Char) : PlacementSet(objectID, ownerID, count,
    uniqueChar
){
    override fun place(game: Game, position: Vector2, dir: Orientation) {
        super.place(game, position, dir)
        val tank = Tank(game.vacantID(), ownerID, position, 6, dir, 3U, 3)
        tank.name = "Middle Tank"
        game.objects.add(tank)
        tank.linkIdentifiers(game)
    }
}
