package guiclient

class Sprite<Context : IRenderingContext>(factory: IRendererFactory<Context>, path : String) : VisualObjectCompositor<Context>(){
    override val renderer: IVisualObjectRenderer<Context> = factory.getSpriteRenderer(this, path)
}