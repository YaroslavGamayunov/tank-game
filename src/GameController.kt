import game.*
import game.actions.GameActionSequence
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

    private var gameActionListeners = ArrayList<GameActionsListener>()
    private var screens = arrayListOf<JFrame>()

    var gameModel: GameModel? = null
    private var server: Server? = null
    var isGuiUsed: Boolean = false

    init {
        System.err.println("GameController created")
        changeScreen(MainScreen())
    }

    private fun changeScreen(screen: JFrame) {
        //currentScreen.dispatchEvent(WindowEvent(currentScreen, WindowEvent.WINDOW_CLOSED));
        if (isGuiUsed) {
            screens.add(screen)
            screen.isVisible = true
        }
    }

    fun getGame(copy: Boolean = true) = if (copy) gameModel?.getGame()?.copy() else gameModel?.getGame()

    fun getLocalPlayer() = gameModel?.localPlayer

    // These 3 methods below are designed for connecting and hosting server by local user
    fun connectToServer(playerName: String, hostName: String, port: Int) {
        gameModel = GameModel(Socket(hostName, port))
        gameModel!!.addPlayer(Player(playerName), isLocalPlayer = true)

        changeScreen(ServerLobbyScreen(hostName, port))
    }

    fun connectToServer(playerName: String, socket: Socket) {
        gameModel = GameModel(socket)
        gameModel!!.addPlayer(Player(playerName), isLocalPlayer = true)

        changeScreen(ServerLobbyScreen(socket.inetAddress.toString(), socket.port))
    }

    fun hostServer(playerName: String, port: Int) {
        server = Server(port, GameServerProcessor())

        val hostName = InetAddress.getLocalHost().hostAddress
        connectToServer(playerName, hostName, port)
    }


    fun onGameStateChanged(state: GameState) {
        for (screen in screens) {
            if (screen is GameStateListener) {
                (screen as GameStateListener).onGameStateChanged(state)
            }
        }
    }


    // sends local player actions to the server
    fun onPlayerMoved(sequence: GameActionSequence) {
        gameModel?.applyActionsToServer(sequence)
    }


    // receives actions from server
    fun onActionsReceived(sequence: GameActionSequence) {
        for (subscriber in gameActionListeners) {
            subscriber.onReceive(sequence)
        }
    }

    fun addGameActionListener(listener: GameActionsListener) {
        gameActionListeners.add(listener)
    }

    fun removeGameActionListener(listener: GameActionsListener) {
        gameActionListeners.remove(listener)
    }


}