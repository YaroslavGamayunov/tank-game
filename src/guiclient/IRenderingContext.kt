package guiclient

interface IRenderingContext {
    fun initContext()
    fun <Context : IRenderingContext> renderObjectTree(root : IVisualObject<Context>)
    val defaultCamera : ICamera<IRenderingContext>
    val factory: IRendererFactory<IRenderingContext>
    fun createFrameInput(deltaTime : Double) : IInput
    fun setTurnState(status : Boolean)
}