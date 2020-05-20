package guiclient

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
    }
}