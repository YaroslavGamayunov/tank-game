package guiclient.swing

import game.tools.Vector2
import guiclient.tools.Transform
import guiclient.tools.Vector3

class SwingDefaultCamera : SwingCamera{
    override val transform = Transform(Vector3(), Vector3(5.0,1.0))

    private fun toScreenCoords(len : Double) : Int{
        return (len * (1.0/ transform.scale.x) * canvasSize.x).toInt()
    }

    private fun toUnitCoords(len: Int) : Double{
        return len * 1.0 * transform.scale.x / canvasSize.x
    }

    fun toScreenCoords(vec : Vector3) : Vector2{
        return Vector2(toScreenCoords(vec.x), -toScreenCoords(vec.y)) + canvasSize / 2
    }
    fun toScreenSize(vec : Vector3) : Vector2{
        return Vector2(toScreenCoords(vec.x), toScreenCoords(vec.y))
    }

    fun toUnitCoords(vec: Vector2) : Vector3{
        val vector = vec - canvasSize/2
        return Vector3(toUnitCoords(vector.x), -toUnitCoords(vector.y))
    }

    var canvasSize : Vector2 = Vector2(0,0)
        set(value){
            field = value
            center = toScreenCoords(transform.position) + canvasSize/2
        }
    var center : Vector2 = Vector2(0,0)
        private set
}