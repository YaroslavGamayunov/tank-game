package guiclient.swing

import guiclient.*
import java.io.File
import javax.imageio.ImageIO

class SwingRendererFactory : IRendererFactory<SwingRenderingContext> {
    override fun getSpriteRenderer(visualObject : Sprite<SwingRenderingContext>, assetName : String) : IVisualObjectRenderer<SwingRenderingContext> {

        val image =  SwingAssetLoader.instance.getImage("/main/resources/sprites/$assetName")
        return SwingSpriteRenderer(visualObject, image);
    }

    override fun getTilemapRenderer(
        tilemap: Sprite<SwingRenderingContext>,
        assetName: String
    ): IVisualObjectRenderer<SwingRenderingContext> {

        val image =  SwingAssetLoader.instance.getImage("/main/resources/sprites/$assetName")
        return SwingTilemapRenderer(tilemap, image)
    }

    override fun getHPRenderer(hpBar: HPBar<SwingRenderingContext>): IVisualObjectRenderer<SwingRenderingContext> {
        return SwingHPRenderer(hpBar)
    }
}