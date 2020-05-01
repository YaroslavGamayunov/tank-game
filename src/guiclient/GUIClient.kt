package guiclient

import game.actions.GameActionSequence
import game.controllers.GameClient
import game.controllers.IGameServerConnector
import game.objects.GamePlayer
import kotlin.concurrent.thread


class GUIClient<RenderingContext : IRenderingContext>(server: IGameServerConnector,
                                                      private val scene : RenderingScene<RenderingContext>,
                                                      private val context: RenderingContext,
                                                      private val factory: IRendererFactory<RenderingContext>
                                                      )
    : GameClient(server) {

    private val renderingThread : Thread
    private var onRender = true
    init {
        context.initContext()
        renderingThread = thread {
            scene.start()
            var time = System.currentTimeMillis()
            while(onRender) {
                Thread.sleep(100)
                val delta = (System.currentTimeMillis() - time) / 1000.0
                scene.update(delta)
                context.renderObjectTree(scene)
            }
            scene.destroy()
        }

        scene.childs.add(Sprite<RenderingContext>(factory, "grass.jpg"))
    }

    override fun makeYourMove(): GameActionSequence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun applyExternalActions(sequence: GameActionSequence) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val owner: GamePlayer
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}