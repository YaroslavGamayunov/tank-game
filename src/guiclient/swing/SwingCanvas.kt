package guiclient.swing

import game.tools.Vector2
import java.awt.Graphics
import javax.swing.JPanel

class SwingCanvas(private val defaultCamera: SwingDefaultCamera) : JPanel() {
    var rootRenderer : SwingObjectRenderer? = null
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        defaultCamera.canvasSize = Vector2(width, height)
        rootRenderer?.let {
            it.render(g, defaultCamera)
        }
    }
}