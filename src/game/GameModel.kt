package game

import GameController
import server.PacketType
import server.ServerConnection
import server.ServerConnectionCallback
import server.ServerPacket
import java.net.Socket

class GameModel(socket: Socket) : ServerConnectionCallback {
    private var connection: ServerConnection = ServerConnection(socket)
    private var state: GameState = GameState()


    init {
        connection.connectionCallback = this
    }

    override fun onReceive(serverPacket: ServerPacket) {
        if (serverPacket.type == PacketType.GAME_STATE) {
            state = serverPacket.payload as GameState
            GameController.instance.onGameStateChanged(state)
        }
        if (serverPacket.type == PacketType.PLAYER_JOINED) {
            var player: Player = serverPacket.payload as Player
            state.players?.put(player.id, player)

            GameController.instance.onGameStateChanged(state)
        }
    }

    fun addPlayer(player: Player) {
        state.players?.put(player.id, player)
        connection.sendData(ServerPacket(PacketType.PLAYER_JOINED, player, shouldBeShared = true))
        GameController.instance.onGameStateChanged(state)
    }

    fun removePlayer(playerId: String) {
        state.players.remove(playerId)
        GameController.instance.onGameStateChanged(state)
    }

    override fun onConnectionInterrupted() {
        TODO("Not yet implemented")
    }
}