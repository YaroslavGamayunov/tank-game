package guiclient.swing

import game.tools.Vector2
import guiclient.tools.Vector3
import logging.logInfo
import java.awt.Graphics
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JPanel

class SwingCanvas(private val defaultCamera: SwingDefaultCamera, private val context: SwingRenderingContext) : JPanel() {
    var rootRenderer : SwingObjectRenderer? = null
    var lastMouseClick : Vector3? = null
    var keys = arrayListOf<Char>()
    init {
        synchronized(context) {
            super.addMouseListener(object : MouseListener {
                override fun mouseReleased(p0: MouseEvent?) {}

                override fun mouseEntered(p0: MouseEvent?) {}

                override fun mouseClicked(p0: MouseEvent?) {
                    synchronized(context) {
                        lastMouseClick = defaultCamera.toUnitCoords(Vector2(p0?.point?.x ?: 0, p0?.point?.y ?: 0))
                    }
                }

                override fun mouseExited(p0: MouseEvent?) {}

                override fun mousePressed(p0: MouseEvent?) {}

            })

            context.frame.addKeyListener(object : KeyListener{
                override fun keyTyped(p0: KeyEvent?) {
                    synchronized(context) {
                        if (p0 != null) {
                            keys.add(p0.keyChar)
                        }
                    }
                }

                override fun keyPressed(p0: KeyEvent?) {

                }

                override fun keyReleased(p0: KeyEvent?) {}

            })
        }
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        synchronized(context) {
            defaultCamera.canvasSize = Vector2(width, height)
            rootRenderer?.let {
                it.render(g, defaultCamera)
            }
        }
    }
}