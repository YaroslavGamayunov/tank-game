package game.objects

import game.objects.GameObject

open class GamePlayer(objectID: Int, val playerName : String) : GameObject(objectID) {
    override fun toString(): String {
        return "{[$playerName ($objectID)]}"
    }
}