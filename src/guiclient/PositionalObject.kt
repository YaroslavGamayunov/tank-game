package guiclient

import game.events.IGameEvent
import game.objects.IGameLocated
import game.units.Tank
import guiclient.tools.Vector3
import logging.logInfo

open class PositionalObject<Context:IRenderingContext>(factory: IRendererFactory<Context>, path: String, val click : (() -> Unit)? = null ) : Sprite<Context>(factory,
    path, false
) {
    override fun update(input: IInput) {
        super.update(input)
        if(input.mouseClick && (input.mousePosition - transform.position).abs < 0.5 ){
            onClick()
        }
    }

    protected open fun onClick(){
        logInfo(this, "Registered click at me")
        click?.invoke()
    }

    fun setPositionByLocated(obj : IGameLocated){
        transform.position = Vector3(obj.position)
    }
}