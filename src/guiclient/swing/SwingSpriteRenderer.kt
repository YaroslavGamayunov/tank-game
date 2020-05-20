package guiclient.swing

import game.tools.Vector2
import guiclient.Sprite
import guiclient.tools.Vector3
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Image
import java.awt.geom.AffineTransform
import java.util.*


open class SwingSpriteRenderer(val sprite : Sprite<SwingRenderingContext>, val image : Image) : SwingObjectRenderer {
    override fun render(graphics: Graphics, camera: SwingDefaultCamera) {
        renderAt(graphics, camera, sprite.transform.position)
    }

    protected fun renderAt(graphics: Graphics, camera: SwingDefaultCamera, position: Vector3) {
        val size = camera.toScreenSize(sprite.transform.scale) + Vector2(1,1)
        val center  = camera.toScreenCoords(position)
        val pos = center - size / 2
        val graphics2d = graphics as Graphics2D
        val trans = AffineTransform()
        trans.translate(pos.x.toDouble(), pos.y.toDouble())
        trans.scale(size.x * 1.0 / image.getWidth(null), size.y * 1.0 / image.getHeight(null) )
        trans.rotate(sprite.transform.rotation, image.getWidth(null)/2.0, image.getHeight(null)/2.0)
        graphics2d.drawImage(image, trans, null)
    }
}