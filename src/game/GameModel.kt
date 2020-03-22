package game

import GameController
import server.PacketType
import server.ServerConnection
import server.ServerConnectionCallback
import server.ServerObject
import java.net.Socket

class GameModel(var socket: Socket) : ServerConnectionCallback {
    private var connection: ServerConnection = ServerConnection(socket)
    private var state: GameState = GameState()


    init {
        connection.connectionCallback = this
    }

    override fun onReceive(serverObject: ServerObject) {
        if (serverObject.type == PacketType.GAME_STATE) {
            state = serverObject.obj as GameState
            GameController.instance.onGameStateChanged(state!!)
        }
        if (serverObject.type == PacketType.PLAYER_JOINED) {
            state?.players?.add(serverObject.obj as Player)
            if (state != null) {
                GameController.instance.onGameStateChanged(state!!)
            }
        }
    }

    fun addPlayer(player: Player) {
        state?.players?.add(player)
        if (state != null) {
            connection.sendData(ServerObject(PacketType.PLAYER_JOINED, player.name, shouldBeShared = true))
            GameController.instance.onGameStateChanged(state!!)
        }
    }

    override fun onConnectionInterrupted() {
        TODO("Not yet implemented")
    }
}