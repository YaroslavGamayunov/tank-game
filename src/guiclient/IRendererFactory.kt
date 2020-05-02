package guiclient

interface IRendererFactory<Context : IRenderingContext>{
    fun getSpriteRenderer(sprite: Sprite<Context>, assetName : String) : IVisualObjectRenderer<Context>
    fun getTilemapRenderer(tilemap: Sprite<Context>, assetName : String) : IVisualObjectRenderer<Context>
}