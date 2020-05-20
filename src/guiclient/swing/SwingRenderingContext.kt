package guiclient.swing

import game.tools.copySerializable
import guiclient.*
import guiclient.tools.Vector3
import javax.swing.JFrame
import kotlin.concurrent.thread

typealias SwingCamera = ICamera<SwingRenderingContext>

open class SwingRenderingContext : IRenderingContext {
    lateinit var canvas: SwingCanvas
    lateinit var frame : JFrame
    val title = "Tank Game by Yaroslav G., Gregory M., Dmitriy P."
    override fun initContext(){
        frame = JFrame(title)
        defaultCamera = SwingDefaultCamera() as ICamera<IRenderingContext>
        canvas = SwingCanvas(defaultCamera as SwingDefaultCamera, this)
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

    override lateinit var defaultCamera: ICamera<IRenderingContext>
    override val factory: IRendererFactory<IRenderingContext> = SwingRendererFactory() as IRendererFactory<IRenderingContext>
    override fun createFrameInput(deltaTime: Double): IInput {
        val input = object : IInput {
            override val mousePosition: Vector3 = canvas.lastMouseClick ?: Vector3()
            override val mouseClick: Boolean = canvas.lastMouseClick != null
            override val deltaTime: Double = deltaTime
            override val keyPressed: ArrayList<Char> = canvas.keys
            override fun <Context : IRenderingContext> getTypedCamera(): ICamera<Context> {
                return defaultCamera as ICamera<Context>
            }
        }

            synchronized(this) {
                canvas.lastMouseClick = null
                canvas.keys = arrayListOf<Char>()
            }

        return input
    }

    override fun setTurnState(status: Boolean) {
        frame.title = title + ( if (status) ": your Turn! Press 'e' on ENG to end turn" else "")
    }
}