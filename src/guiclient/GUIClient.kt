package guiclient

import game.actions.GameActionSequence
import game.controllers.CLIGameClient
import game.controllers.GameClient
import game.controllers.IGameServerConnector
import game.objects.GamePlayer
import game.units.Tank
import guiclient.tools.Vector3
import game.tools.Orientation
import kotlin.concurrent.thread


class GUIClient<RenderingContext : IRenderingContext>(server: IGameServerConnector,
                                                      private val scene : RenderingScene<RenderingContext>,
                                                      private val context: RenderingContext
                                                      )
    : CLIGameClient(server) {

    private val renderingThread : Thread
    private val factory: IRendererFactory<RenderingContext>
    private val gameObjects = hashMapOf<Int, Sprite<RenderingContext>>()
    private val rotationToAngle = hashMapOf<Orientation, Double>(Orientation.UP to 0.0, Orientation.DOWN to 3.1415, Orientation.LEFT to -3.1415 / 2, Orientation.RIGHT to 3.1415/2)
    private var onRender = true
    init {
        context.initContext()
        renderingThread = thread {
            scene.start()
            var time = System.currentTimeMillis()
            while(onRender) {
                val delta = (System.currentTimeMillis() - time) / 1000.0
                synchronized(context) {
                    scene.update(delta)
                }
                context.renderObjectTree(scene)
            }
            scene.destroy()
        }
        factory = context.factory as IRendererFactory<RenderingContext>
        renderState()
    }

    fun renderState(){
        scene.childs.clear()
        scene.childs.add(Sprite<RenderingContext>(factory, "grass.jpg",true))
        for(obj in game.objects){
            if(obj is Tank){
                val tankSprite = Sprite<RenderingContext>(factory, "tank.jpg",false)
                gameObjects[obj.objectID] = tankSprite
                tankSprite.transform.position = Vector3(obj.position)
                tankSprite.transform.rotation = rotationToAngle[obj.orientation]!!
                scene.childs.add(tankSprite)
            }
        }
    }

    override fun gameStateChange(){
        synchronized(context) {
            renderState()
        }
    }

}