import game.DebugServerProcessor
import game.GameServerProcessor
import game.controllers.*
import server.Server
import java.net.InetAddress
import java.net.Socket

fun viewDocs(){
    println("This it Tank Game developed by Yaroslav G. Gregory M. Dmitriy P.");
    println("Use: -help \t to view this guide");
    println("Use: -server [port]\t to run server");
    println("Use: -client [nickname] [server:port] [cli|gui]\t to run connect to the server");
    println()
}

fun main(args: Array<String>) {
    if(args.isEmpty()){
        return viewDocs()
    }

    when(args[0]){
        "-help"->{
            return viewDocs()
        }

        "-server"->{
            if(args.size < 2) return viewDocs()
            val port = args[1].toInt()
            val server = Server(port, GameServerProcessor())
        }

        "-debugServer"->{
            val port = 100
            val server = Server(port, DebugServerProcessor())
        }

        "-client"->{
            if(args.size < 4) return viewDocs()
            val parts = args[2].split(':')
            val hostName = parts[0]
            val port = parts[1].toInt()
            val connector = MultiplayerConnector(Socket(hostName, port), args[1])
            when(args[3]){
                "cli"->{
                    return connector.runConnector(CLIClientFactory())
                }

                "gui"->{
                   return connector.runConnector(SwingClientFactory())
                }
            }

        }
    }

}


/*
fun runGuiSinglePlayer(){
    val connector = LocalSinglePlayerConnector()
    val swingContext = SwingRenderingContext()
    val scene = RenderingScene<SwingRenderingContext>()
    scene.renderer = SwingSceneRenderer(scene)
    val client = GUIClient<SwingRenderingContext>(connector, scene, swingContext)
    connector.runConnector(client)
}
*/

/*
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

*/

fun runMultiplayerServer() {
    GameController.instance.isGuiUsed = false
    println("Do you want to host a server? [Y/n]")
    var response = readLine() ?: ""
    var server: Server? = null
    var connector: MultiplayerConnector? = null
    if (response.toLowerCase().equals("y")) {
        println("Write down your name:")
        var name = readLine() ?: ""
        println("Ok, then type a server port (0 - 65535):")
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

    connector.runConnector(SwingClientFactory())

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
    connector.runConnector(CLIClientFactory())
}