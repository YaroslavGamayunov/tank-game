package game

import GameController
import game.actions.GameActionSequence
import game.objects.GamePlayer
import server.PacketType
import server.ServerConnection
import server.ServerConnectionCallback
import server.ServerPacket
import java.net.Socket

class GameModel(socket: Socket) : ServerConnectionCallback {
    private var connection: ServerConnection = ServerConnection(socket)
    var state: GameState = GameState()
    var localPlayer: Player? = null

    init {
        connection.connectionCallback = this
    }

    override fun onReceive(serverPacket: ServerPacket) {
        when (serverPacket.type) {
            PacketType.GAME_STATE -> {
                state = serverPacket.payload as GameState

                if (localPlayer != null) {
                    localPlayer = state.players[localPlayer?.id]
                }
            }

            PacketType.PLAYER_JOINED -> {
                var player: Player = serverPacket.payload as Player
                state.players[player.id] = player
            }

            PacketType.PLAYER_LEFT -> removePlayer(serverPacket.payload as String)

            PacketType.PLAYER_MOVED -> applyPlayerActions(serverPacket.payload as GameActionSequence)
        }

        GameController.instance.onGameStateChanged(state)
    }

    override fun onConnectionInterrupted() {}


    fun applyPlayerActions(sequence: GameActionSequence) {
        for (action in sequence.actions) {
            TODO("implement")
        }
        GameController.instance.onGameStateChanged(state)
    }

    fun getGameCopy() = state.game?.copy()

    fun addPlayer(player: Player, isLocalPlayer: Boolean = false) {
        if (isLocalPlayer) {
            localPlayer = player
        }
        state.players[player.id] = player
        connection.sendData(ServerPacket(PacketType.PLAYER_JOINED, player, shouldBeShared = true))
        GameController.instance.onGameStateChanged(state)
    }

    fun removePlayer(playerId: String) {
        state.players.remove(playerId)
        GameController.instance.onGameStateChanged(state)
    }
}