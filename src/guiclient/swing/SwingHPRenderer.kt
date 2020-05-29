package guiclient.swing

import game.units.tanks.Tank
import guiclient.HPBar
import guiclient.tools.Vector3
import java.awt.*


open class SwingHPRenderer(val hpBar: HPBar<SwingRenderingContext>) : SwingObjectRenderer {
    override fun render(graphics: Graphics, camera: SwingDefaultCamera) {
        renderBar(graphics, camera, 1.0, Color.RED)
        renderBar(graphics, camera, hpBar.percent, Color.GREEN)
        val position = camera.toScreenCoords(hpBar.transform.position + Vector3(-0.41 *(1), 0.5))
        renderCaption(graphics, camera)
    }

    fun renderCaption(graphics: Graphics, camera: SwingDefaultCamera){
        if(hpBar.unit is Tank){
            val size = camera.toScreenSize(Vector3(0.8 * 1, 0.2))
            val position = camera.toScreenCoords(hpBar.transform.position + Vector3(-0.41 *(1), 0.5))
            graphics.color = Color.BLACK
            drawCenteredString(graphics, "${hpBar.unit.name}",  Rectangle(position.x, position.y, size.x, size.y), Font("Arial", Font.PLAIN, size.y /2) )
        }
    }

    fun renderBar(graphics: Graphics, camera: SwingDefaultCamera, len : Double, color: Color){
        val size = camera.toScreenSize(Vector3(0.8 * len, 0.2))
        val position = camera.toScreenCoords(hpBar.transform.position + Vector3(-0.41 *(1- len), 0.5)) - size/2
        graphics.color = color
        graphics.fillRect(position.x, position.y, size.x, size.y)
    }

    open fun drawCenteredString(g: Graphics, text: String?, rect: Rectangle, font: Font?) {
        val metrics: FontMetrics = g.getFontMetrics(font)
        val x: Int = rect.x + (rect.width - metrics.stringWidth(text)) / 2
        val y: Int = rect.y + (rect.height - metrics.getHeight()) / 2 + metrics.getAscent() / 2
        g.font = font
        g.drawString(text, x, y)
    }
}