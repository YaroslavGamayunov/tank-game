package guiclient.swing

import java.awt.Canvas
import java.awt.Graphics

class SwingCanvas : Canvas() {
    var rootRenderer : SwingObjectRenderer? = null
    override fun paint(g: Graphics) {
        super.paint(g)
        rootRenderer?.let {
            it.render(g)
        }
    }
}