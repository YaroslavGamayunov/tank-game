package guiclient

interface IRenderingContext {
    fun initContext()
    fun <Context : IRenderingContext> renderObjectTree(root : IVisualObject<Context>)
    val defaultCamera : ICamera<IRenderingContext>
    val factory: IRendererFactory<IRenderingContext>
}