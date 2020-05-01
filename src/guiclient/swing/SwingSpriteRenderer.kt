package guiclient.swing

import guiclient.IVisualObject
import guiclient.Sprite
import java.awt.Graphics
import java.awt.Image

class SwingSpriteRenderer(val visualObject : Sprite<SwingRenderingContext>, val sprite : Image) : SwingObjectRenderer {
    override fun render(graphics: Graphics) {
        graphics.drawImage(sprite, 0, 0, null)
    }
}