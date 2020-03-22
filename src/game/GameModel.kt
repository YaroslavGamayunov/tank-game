package game

import GameController
import server.PacketType
import server.ServerConnection
import server.ServerConnectionCallback
import server.ServerObject
import java.net.Socket

class GameModel(var socket: Socket) : ServerConnectionCallback {
    private var connection: ServerConnection = ServerConnection(socket)
    private var state: GameState? = null


    init {
        connection.connectionCallback = this
    }

    override fun onReceive(serverObject: ServerObject) {
        if (serverObject.type == PacketType.GAME_STATE) {
            state = serverObject.obj as GameState
            GameController.instance.changeGameState(state!!)
        }
        if (serverObject.type == PacketType.CONNECTION_DATA) {
            state?.players?.add(serverObject.obj as String)
            if (state != null) {
                GameController.instance.changeGameState(state!!)
            }
        }
    }

    fun addPlayer(name: String) {
        state?.players?.add(name)
        if (state != null) {
            GameController.instance.changeGameState(state!!)
        }
    }

    override fun onConnectionInterrupted() {
        TODO("Not yet implemented")
    }
}