import game.GameModel
import game.GameServerProcessor
import game.GameState
import game.Player
import gui.GameStateListener
import gui.MainScreen
import gui.ServerLobbyScreen
import server.*
import java.net.InetAddress
import java.net.Socket
import javax.swing.JFrame

class GameController private constructor() {

    private object HOLDER {
        val INSTANCE = GameController()
    }

    companion object {
        val instance: GameController by lazy { HOLDER.INSTANCE }
    }

    private var screens = arrayListOf<JFrame>(MainScreen())

    private var gameModel: GameModel? = null
    private var server: Server? = null

    init {
        println("GameController created")
    }

    private fun changeScreen(screen: JFrame) {
        //currentScreen.dispatchEvent(WindowEvent(currentScreen, WindowEvent.WINDOW_CLOSED));
        screens.add(screen)
    }

    fun getGameCopy() = gameModel?.getGameCopy()

    // These 3 methods below are designed for connecting and hosting server by local user
    fun connectToServer(playerName: String, hostName: String, port: Int) {
        gameModel = GameModel(Socket(hostName, port))
        changeScreen(ServerLobbyScreen(hostName, port))
        gameModel!!.addPlayer(Player(playerName), isLocalPlayer = true)
    }

    fun connectToServer(playerName: String, socket: Socket) {
        gameModel = GameModel(socket)
        changeScreen(ServerLobbyScreen(socket.inetAddress.toString(), socket.port))
        gameModel!!.addPlayer(Player(playerName), isLocalPlayer = true)
    }

    fun hostServer(playerName: String, port: Int) {
        server = Server(port, GameServerProcessor())

        val hostName = InetAddress.getLocalHost().hostAddress
        gameModel = GameModel(Socket(hostName, port))
        changeScreen(ServerLobbyScreen(hostName, port))
        gameModel!!.addPlayer(Player(playerName), isLocalPlayer = true)
    }

    fun onGameStateChanged(state: GameState) {
        for (screen in screens) {
            if (screen is GameStateListener) {
                (screen as GameStateListener).onGameStateChanged(state)
            }
        }
    }
}