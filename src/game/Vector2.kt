package game
import java.io.Serializable
import java.lang.RuntimeException
import kotlin.math.abs

data class Vector2(val x : Int, val y : Int) : Serializable{
    override fun toString() : String{
        return "{$x,$y}"
    }

    operator fun plus(other: Vector2) : Vector2{
        return Vector2(x + other.x, y + other.y)
    }

    operator fun minus(other: Vector2) : Vector2{
        return Vector2(x - other.x, y - other.y)
    }

    val orientation : Orientation?
        get(){
            val absolute = manhattanAbs
            val or = Vector2(x / absolute, y / absolute)
            for (x in Orientation.values()) {
                if (x.direction == or) return x
            }
            return null
        }
    val manhattanAbs : Int
       get(){
           return abs(x) + abs(y)
       }

}