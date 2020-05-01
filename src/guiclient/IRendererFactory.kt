package guiclient

interface IRendererFactory<Context : IRenderingContext>{
    fun getSpriteRenderer(sprite: Sprite<Context>, assetName : String) : IVisualObjectRenderer<Context>
}