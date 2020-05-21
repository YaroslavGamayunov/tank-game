package game.objects

import game.objects.GameObject

open class GamePlayer(objectID: Int) : GameObject(objectID) {
    override fun toString(): String {
        return "{[Player] id: $objectID}"
    }
}