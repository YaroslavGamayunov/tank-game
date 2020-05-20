package guiclient

import guiclient.tools.Vector3
import logging.logInfo

open class RenderingScene<Context : IRenderingContext>
    : VisualObjectCompositor<Context>() {
    override lateinit var renderer: IVisualObjectRenderer<Context>
    lateinit var client : GUIClient<Context>
    override fun update(input: IInput) {
        super.update(input)
        if(input.mouseClick){
           logInfo(this, "Mouse click at ${input.mousePosition.toString()}")
        }

        if('e' in input.keyPressed){
            logInfo(this, "Pressed E so it is end of turn")
            client.wait = false
        }

        val camera =  input.getTypedCamera<Context>()
        val moveCoef = 0.1;
        val scaleCoef = 0.9;
        if('w' in input.keyPressed) {
            camera.transform.position += Vector3(0.0, 1.0) * moveCoef
        }
        if('s' in input.keyPressed){
            camera.transform.position += Vector3(0.0, -1.0) *moveCoef
        }
        if('a' in input.keyPressed) {
            camera.transform.position += Vector3(-1.0, 0.0)*moveCoef
        }
        if('d' in input.keyPressed){
            camera.transform.position += Vector3(1.0, 0.0)*moveCoef
        }

        if('1' in input.keyPressed){
            camera.transform.scale *= scaleCoef
        }

        if('2' in input.keyPressed){
            camera.transform.scale *= 1.0 / scaleCoef
        }

     //   if('w' in input.keyPressed)

    }
}