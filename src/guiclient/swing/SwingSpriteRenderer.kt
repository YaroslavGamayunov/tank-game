package guiclient.swing

import game.tools.Vector2
import guiclient.IVisualObject
import guiclient.Sprite
import java.awt.Graphics
import java.awt.Image

open class SwingSpriteRenderer(val sprite : Sprite<SwingRenderingContext>, val image : Image) : SwingObjectRenderer {
    override fun render(graphics: Graphics, camera: SwingDefaultCamera) {
        val size = camera.toScreenCoords(sprite.transform.scale)
        val pos = camera.toScreenCoords(sprite.transform.position) + camera.center - size / 2
        graphics.drawImage(image, pos.x, pos.y, size.x, size.y, null)
    }


}