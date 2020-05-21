package game.units

import game.objects.GameObject
import game.objects.IGameLocated
import game.tools.Vector2

class Obstacle(objectID: Int, override val position: Vector2) : GameObject(objectID), IGameLocated {
    override val solid = true
}