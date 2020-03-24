import game.GameModel
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
        var processor = object : ServerIncomingPacketProcessor {
            var PlayerIdByAddr = HashMap<InetAddress, String>()
            var globalGameState = GameState()
            override fun onReceive(connection: ServerConnection, packet: ServerPacket): Boolean {

                if (packet.type == PacketType.PLAYER_JOINED) {
                    var player = packet.payload as Player
                    PlayerIdByAddr[connection.getAddress()] = (packet.payload as Player).id
                    globalGameState.players[player.id] = player
                    connection.sendData(ServerPacket(PacketType.GAME_STATE, globalGameState))
                }

                return packet.shouldBeShared
            }

            override fun onConnectionInterrupted(connection: ServerConnection): ServerPacket? {
                var disconnectedPlayerId: String? = PlayerIdByAddr[connection.getAddress()]

                if (disconnectedPlayerId != null) {
                    PlayerIdByAddr.remove(connection.getAddress())
                    globalGameState.players.remove(disconnectedPlayerId)
                    return ServerPacket(PacketType.PLAYER_LEFT, disconnectedPlayerId, true)
                }
                return null
            }
        }

        server = Server(port, processor)

        val hostName = InetAddress.getLocalHost().hostAddress
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
}