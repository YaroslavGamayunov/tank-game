import game.controllers.LocalSinglePlayerConnector
import game.controllers.CLIGameClient
import game.controllers.LocalMultiplayerConnector
import game.controllers.LocalMultiplayerServer
import game.tools.Vector2
import guiclient.GUIClient
import guiclient.RenderingScene
import guiclient.swing.SwingRendererFactory
import guiclient.swing.SwingRenderingContext
import guiclient.swing.SwingSceneRenderer

fun main(){
    runGUIMult()
}

fun runGuiSinglePlayer(){
    val connector = LocalSinglePlayerConnector()
    val swingContext = SwingRenderingContext()
    val scene = RenderingScene<SwingRenderingContext>()
    scene.renderer = SwingSceneRenderer(scene)
    val client = GUIClient<SwingRenderingContext>(connector, scene, swingContext)
    connector.runConnector(client)
}

fun runGUIMult(){
    val server = LocalMultiplayerServer()

    val connector1 = LocalMultiplayerConnector(server, tankPosition = Vector2(0,0))
    val swingContext1 = SwingRenderingContext()
    val scene1 = RenderingScene<SwingRenderingContext>()
    scene1.renderer = SwingSceneRenderer(scene1)
    val client1 = GUIClient<SwingRenderingContext>(connector1, scene1, swingContext1)
    connector1.runConnector(client1)

    val connector2 = LocalMultiplayerConnector(server, tankPosition = Vector2(3,0))
    val swingContext2 = SwingRenderingContext()
    val scene2 = RenderingScene<SwingRenderingContext>()
    scene2.renderer = SwingSceneRenderer(scene2)
    val client2 = GUIClient<SwingRenderingContext>(connector2, scene2, swingContext2)
    connector2.runConnector(client2)
/*
    val connector2 = LocalMultiplayerConnector(server, tankPosition = Vector2(3,0))
    val client2 = CLIGameClient(connector2)
    connector2.runConnector(client2)
*/
    server.startGame()
}

fun runMultiplayer(){
    val server = LocalMultiplayerServer()

    
    val connector1 = LocalMultiplayerConnector(server, tankPosition = Vector2(0,0))
    val client1 = CLIGameClient(connector1)
    connector1.runConnector(client1)

    val connector2 = LocalMultiplayerConnector(server, tankPosition = Vector2(10,0))
    val client2 = CLIGameClient(connector2)
    connector2.runConnector(client2)

    server.startGame()
}

fun runSinglePlayer(){
    val connector = LocalSinglePlayerConnector()
    val client = CLIGameClient(connector)
    connector.runConnector(client)
}