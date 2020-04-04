import game.controllers.LocalSinglePlayerConnector
import game.controllers.CLIGameClient
import game.controllers.LocalMultiplayerConnector
import game.controllers.LocalMultiplayerServer
import game.tools.Vector2

fun main(){
    runMultiplayer()
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