package guiclient

import guiclient.tools.Transform

interface IVisualObject<Context: IRenderingContext> {
    fun start()
    fun update(deltaTime : Double)
    fun destroy()

    val childs : ArrayList<IVisualObject<Context>>
    val renderer : IVisualObjectRenderer<Context>
    val transform : Transform
}