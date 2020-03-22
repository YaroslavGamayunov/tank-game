import game.GameModel
import game.GameState
import game.Player
import gui.GameStateListener
import gui.MainScreen
import gui.ServerLobbyScreen
import server.Server
import server.ServerConnection
import java.awt.event.WindowEvent
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

    }

    private fun changeScreen(screen: JFrame) {
        //currentScreen.dispatchEvent(WindowEvent(currentScreen, WindowEvent.WINDOW_CLOSED));
        screens.add(screen)
    }

    fun connectToServer(playerName: String, hostName: String, port: Int) {
        gameModel = GameModel(Socket(hostName, port))
        changeScreen(ServerLobbyScreen(hostName, port))
        gameModel!!.addPlayer(Player(playerName))
    }

    fun hostServer(playerName: String, port: Int) {
        server = Server(port)
        val hostName = InetAddress.getLocalHost().hostAddress;
        gameModel = GameModel(Socket(hostName, port))
        changeScreen(ServerLobbyScreen(hostName, port))
        gameModel!!.addPlayer(Player(playerName))
    }

    fun onGameStateChanged(state: GameState) {
        for (screen in screens) {
            if (screen is GameStateListener) {
                (screen as GameStateListener).onGameStateChanged(state)
            }
        }
    }

    fun notifyPlayerLeft(connection: ServerConnection) {

    }
}