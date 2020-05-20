package guiclient.swing

import guiclient.HPBar
import guiclient.Sprite
import guiclient.tools.Vector3
import java.awt.Color
import java.awt.Graphics
import java.awt.Image

open class SwingHPRenderer(val hpBar: HPBar<SwingRenderingContext>) : SwingObjectRenderer {
    override fun render(graphics: Graphics, camera: SwingDefaultCamera) {
        renderBar(graphics, camera, 1.0, Color.RED)
        renderBar(graphics, camera, hpBar.percent, Color.GREEN)


    }

    fun renderBar(graphics: Graphics, camera: SwingDefaultCamera, len : Double, color: Color){
        val size = camera.toScreenSize(Vector3(0.8 * len, 0.2))
        val position = camera.toScreenCoords(hpBar.transform.position + Vector3(-0.41 *(1- len), 0.5)) - size/2
        graphics.color = color
        graphics.fillRect(position.x, position.y, size.x, size.y)
    }
}