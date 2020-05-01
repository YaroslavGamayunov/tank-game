package guiclient

interface IRenderingContext {
    fun initContext()
    fun <Context : IRenderingContext> renderObjectTree(root : IVisualObject<Context>)
}