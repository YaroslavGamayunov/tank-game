package guiclient.swing

import guiclient.IRendererFactory
import guiclient.IRenderingContext
import guiclient.IVisualObject
import javax.swing.JFrame

open class SwingRenderingContext : IRenderingContext {
    lateinit var canvas: SwingCanvas
    override fun initContext(){
        val frame = JFrame("Tank Game by Yaroslav G., Gregory M., Dmitriy P.")
        canvas = SwingCanvas()
        canvas.setSize(800, 800)
        frame.add(canvas)
        frame.pack()
        frame.isVisible = true
    }

    override fun <Context : IRenderingContext> renderObjectTree(root: IVisualObject<Context>) {
        val swingRoot = root.renderer as SwingObjectRenderer
        canvas.rootRenderer = swingRoot
        canvas.repaint()
    }


}