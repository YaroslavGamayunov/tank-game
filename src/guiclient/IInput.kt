package guiclient

import guiclient.tools.Vector3

interface IInput {
    val mousePosition: Vector3
    val mouseClick : Boolean
    val deltaTime : Double
    val keyPressed : ArrayList<Char>

}