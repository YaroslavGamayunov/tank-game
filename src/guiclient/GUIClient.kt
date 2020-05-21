package guiclient

import game.actions.*
import game.controllers.CLIGameClient
import game.controllers.GameClient
import game.controllers.IGameServerConnector
import game.objects.GamePlayer
import game.objects.IGameLocated
import game.units.Tank
import guiclient.tools.Vector3
import game.tools.Orientation
import game.tools.Vector2
import game.units.Obstacle
import kotlin.concurrent.thread


class GUIClient<RenderingContext : IRenderingContext>(server: IGameServerConnector,
                                                      private val scene : RenderingScene<RenderingContext>,
                                                      private val context: RenderingContext
                                                      )
    : CLIGameClient(server) {

    private val renderingThread : Thread
    private val factory: IRendererFactory<RenderingContext>
    private val gameObjects = hashMapOf<Int, Sprite<RenderingContext>>()
    private val markers = ArrayList<PositionalObject<RenderingContext>>()
    var wait : Boolean = false
    private val rotationToAngle = hashMapOf<Orientation, Double>(Orientation.UP to 0.0, Orientation.DOWN to 3.1415, Orientation.LEFT to -3.1415 / 2, Orientation.RIGHT to 3.1415/2)
    private var onRender = true
    init {
        context.initContext()
        scene.client = this
        renderingThread = thread {
            scene.start()
            var time = System.currentTimeMillis()
            while(onRender) {
                val delta = (System.currentTimeMillis() - time) / 1000.0
                synchronized(context) {
                    scene.update(context.createFrameInput(delta))
                }
                context.renderObjectTree(scene)
            }
            scene.destroy()
        }
        factory = context.factory as IRendererFactory<RenderingContext>
        renderState()
    }

    fun contextSync(action : ()->Unit?){
        thread {
            synchronized(context) {
                action()
            }
        }
    }

    override fun makeYourMove() : GameActionSequence {
        lastSequence = GameActionSequence(owner.objectID)
        wait = true
        context.setTurnState(true)
        while(wait){
            Thread.sleep(100)
        }
        applyMyAction( MoveEnd(owner.objectID), lastSequence)
        context.setTurnState(false)
        return lastSequence
    }

    fun renderState() = contextSync{
        removeMarkers()
        scene.childs.clear()
        scene.childs.add(Sprite<RenderingContext>(factory, "grass.jpg",true))
        for(obj in game.objects){
            if (obj is Tank) {
                val tankSprite = PositionalObject<RenderingContext>(factory, if(obj.owner == owner) "tank.png" else "tankEnemy.png") {
                    if(game.currentMovePlayer != owner.objectID) return@PositionalObject
                    contextSync {

                            removeMarkers()
                            // Place shoot pointers
                            for (aim in game.objects) {
                                val shoot = TankShoot(obj.objectID, aim.objectID)
                                try {
                                    shoot.invoke(game, true)
                                    val pointer = PositionalObject<RenderingContext>(factory, "act_move.png"){
                                        contextSync {
                                            applyMyAction(shoot, lastSequence)
                                        }
                                    }
                                    pointer.setPositionByLocated(aim as IGameLocated)
                                    scene.childs.add(pointer)
                                    markers.add(pointer)
                                } catch (ex: IllegalActionException) {
                                }
                            }

                        for(orientation in Orientation.values()){
                            val rotateTank = RotateTank(obj.objectID, orientation)
                            try{
                                rotateTank.invoke(game, true)
                                val pointer = PositionalObject<RenderingContext>(factory, "act_move.png"){
                                    contextSync {
                                        applyMyAction(rotateTank, lastSequence)
                                    }
                                }
                                pointer.transform.position = Vector3(obj.position + orientation.direction)
                                scene.childs.add(pointer)
                                markers.add(pointer)
                            } catch (ex: IllegalActionException) {
                            }

                        }

                        for( x in -100..100) {
                            for (y in -100..100) {
                                val moveTank = MoveTank(obj.objectID, Vector2(x,y))
                                try{
                                    moveTank.invoke(game, true)
                                    val pointer = PositionalObject<RenderingContext>(factory, "act_move.png"){
                                        contextSync {
                                            applyMyAction(moveTank, lastSequence)
                                        }
                                    }
                                    pointer.transform.position = Vector3(x*1.0,y*1.0)
                                    scene.childs.add(pointer)
                                    markers.add(pointer)
                                } catch (ex: IllegalActionException) {
                                }
                            }
                        }
                    }
                }
                tankSprite.setPositionByLocated(obj)
                gameObjects[obj.objectID] = tankSprite
                tankSprite.transform.rotation = rotationToAngle[obj.orientation]!!
                scene.childs.add(tankSprite)
                scene.childs.add(HPBar<RenderingContext>(factory, obj, tankSprite.transform.position))
            }

            if (obj is Obstacle) {
                val obstacleSprite = PositionalObject<RenderingContext>(factory, "box.jpg")
                obstacleSprite.setPositionByLocated(obj)
                gameObjects[obj.objectID] = obstacleSprite
                scene.childs.add(obstacleSprite)
            }
        }
    }

    fun removeMarkers(){
        for(marker in markers){
            scene.childs.remove(marker)
        }
        markers.clear()
    }

    override fun gameStateChange(){
        renderState()
    }

}