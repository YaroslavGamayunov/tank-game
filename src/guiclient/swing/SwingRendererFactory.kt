package guiclient.swing

import guiclient.IRendererFactory
import guiclient.IVisualObject
import guiclient.IVisualObjectRenderer
import guiclient.Sprite
import java.io.File
import javax.imageio.ImageIO

class SwingRendererFactory : IRendererFactory<SwingRenderingContext> {
    override fun getSpriteRenderer(visualObject : Sprite<SwingRenderingContext>, assetName : String) : IVisualObjectRenderer<SwingRenderingContext> {
        val image = ImageIO.read(File("assets/sprites/$assetName"))
        return SwingSpriteRenderer(visualObject, image);
    }
}