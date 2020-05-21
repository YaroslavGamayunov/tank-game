package guiclient

import guiclient.tools.Transform
import guiclient.tools.Vector3
import logging.logInfo

open abstract  class VisualObjectCompositor<Context : IRenderingContext> : IVisualObject<Context> {
    override fun start() {
        logInfo(this, "${this.javaClass.kotlin.simpleName} received start callback")
        callAllChilds {
            it.start()
        }
    }

    override fun update(input : IInput) {
        callAllChilds {
            it.update(input)
        }
    }

    override fun destroy() {
        logInfo(this, "${this.javaClass.kotlin.simpleName} received destroy callback")
        callAllChilds {
            it.destroy()
        }
    }

    protected fun callAllChilds(action : (IVisualObject<Context>)-> Unit){
        for(obj in childs){
           action(obj)
        }
    }

    override val childs: ArrayList<IVisualObject<Context>> = arrayListOf()
    override val transform : Transform = Transform(Vector3(), Vector3(1.0,1.0,1.0))
}