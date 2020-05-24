package guiclient.swing

import guiclient.IVisualObjectRenderer
import java.awt.Graphics

interface SwingObjectRenderer :
    IVisualObjectRenderer<SwingRenderingContext> {
    fun render(graphics: Graphics, camera: SwingDefaultCamera)

}