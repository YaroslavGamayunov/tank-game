import game.GameModel
import game.GameState
import gui.GameStateListener
import gui.MainScreen
import gui.ServerLobbyScreen
import server.Server
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
        gameModel!!.addPlayer(playerName)
        screens.add(ServerLobbyScreen(hostName, port))
    }

    fun hostServer(playerName: String, port: Int) {
        server = Server(port)
        val hostName = InetAddress.getLocalHost().hostAddress;
        gameModel = GameModel(Socket(hostName, port))
        screens.add(ServerLobbyScreen(hostName, port))
        gameModel!!.addPlayer(playerName)
    }

    fun changeGameState(state: GameState) {
        for (screen in screens) {
            if (screen is GameStateListener) {
                (screen as GameStateListener).onGameStateChanged(state)
            }
        }
    }
}