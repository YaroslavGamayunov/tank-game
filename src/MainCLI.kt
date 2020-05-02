import game.GameServerProcessor
import game.controllers.*
import game.tools.Vector2
import server.Server
import java.net.InetAddress
import java.net.Socket

fun main() {
    runMultiplayer()
}

fun runMultiplayer() {
    GameController.instance.isGuiUsed = false
    println("Do you want to host a server? [Y/n]")
    var response = readLine() ?: ""
    var server: Server? = null
    var connector: MultiplayerConnector? = null
    if (response.toLowerCase().equals("y")) {
        println("Write down your name:")
        var name = readLine() ?: ""
        println("Ok, then type a server port (0 - 65536):")
        var port = readLine()?.toInt() ?: 1234
        server = Server(port, GameServerProcessor())
        connector = MultiplayerConnector(Socket(InetAddress.getLocalHost().hostAddress, port), name)

    } else {
        println("Then you have to connect to a server")
        println("Write down your name:")
        var name = readLine() ?: ""
        println("Type address and port of server separated by ':' :")
        var serverAddress = readLine() ?: ""
        var parts = serverAddress.split(':')
        var hostName = parts[0]
        var port = parts[1].toInt()
        connector = MultiplayerConnector(Socket(hostName, port), name)
    }


//    val connector1 = LocalMultiplayerConnector(server, tankPosition = Vector2(0, 0))
//    val client1 = CLIGameClient(connector1)
//    connector1.runConnector(client1)
//
//    val connector2 = LocalMultiplayerConnector(server, tankPosition = Vector2(10, 0))
//    val client2 = CLIGameClient(connector2)
//    connector2.runConnector(client2)
//
//    server.startGame()
}

fun runSinglePlayer() {
    val connector = LocalSinglePlayerConnector()
    val client = CLIGameClient(connector)
    connector.runConnector(client)
}