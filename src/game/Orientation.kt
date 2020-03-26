package game
import game.Vector2

enum class Orientation(val direction: Vector2) {
    UP(Vector2(0,1)),
    DOWN(Vector2(0,-1)),
    LEFT(Vector2(-1,0)),
    RIGHT(Vector2(1,0))
}