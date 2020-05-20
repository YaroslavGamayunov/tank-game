package guiclient.tools

import game.tools.Vector2
import kotlin.math.sqrt

data class Vector3(val x : Double = 0.0, val y : Double = 0.0, val z : Double = 0.0){
    constructor(vector2: Vector2, z : Double = 0.0) : this(vector2.x.toDouble(), vector2.y.toDouble() , z)

    override fun toString() : String{
        return "{$x,$y,$z}"
    }

    operator fun plus(other: Vector3) :  Vector3 {
        return Vector3(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Vector3) : Vector3 {
        return Vector3(x - other.x, y - other.y, z + other.z)
    }

    operator fun times(scalar : Double) : Vector3{
        return Vector3(x * scalar, y *scalar, z * scalar)
    }

    val abs : Double
    get() {return sqrt(x*x + y*y+z*z)}
}

operator fun Double.times(vector3 : Vector3) : Vector3{
    return vector3 * this
}