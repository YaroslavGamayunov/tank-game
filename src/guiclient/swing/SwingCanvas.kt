package guiclient.swing

import java.awt.Canvas
import java.awt.Graphics
import javax.swing.JPanel

class SwingCanvas : JPanel() {
    var rootRenderer : SwingObjectRenderer? = null
    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        rootRenderer?.let {
            it.render(g)
        }
    }
}