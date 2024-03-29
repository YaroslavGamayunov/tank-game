package game

import GameController
import game.actions.GameActionSequence
import game.actions.applyAllActions
import logging.logInfo
import server.*
import java.net.Socket

class GameModel(socket: Socket) : ServerConnectionCallback {
    private var connection: ServerConnection = ServerConnection(socket)
    var state: GameState = GameState()
        set(state) {
            for (gameObject in state.game?.objects!!) {
                state.game?.let {
                    gameObject.linkIdentifiers(state.game!!)
                }
            }
            field = state
        }
    var localPlayer: Player? = null
    var isGameStateReceived: Boolean = false
        private set

    init {
        connection.connectionCallback = this
    }

    // TODO: Create callbacks for move demanded and gamestatechanged
    override fun onReceive(serverPacket: ServerPacket) {
        System.err.println("Game model received object from server: ${serverPacket}")
        printPayload(serverPacket)
        
        when (serverPacket.type) {
            PacketType.GAME_STATE -> {
                isGameStateReceived = true
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

            PacketType.SHARED_ACTIONS -> applyPlayerActions(serverPacket.payload as GameActionSequence)
        }
        GameController.instance.onGameStateChanged(state)
    }

    override fun onConnectionInterrupted() {}


    private fun applyPlayerActions(sequence: GameActionSequence) {
        var game: Game = state.game ?: return
        // Here we have to lock on any subscriber
        // This is a quite fragile trick but this is only way ....

        val any = GameController.instance.gameActionListeners.first()
        logInfo(this, "Syncing on $any")
        synchronized(any) {
            logInfo(this, "Start processing upcoming events")
            for (action in sequence.actions) {
                applyAllActions(game, action)
            }

            GameController.instance.onActionsReceived(sequence)
        }
    }

    fun applyActionsToServer(sequence: GameActionSequence) {
        connection.sendData(ServerPacket(PacketType.SHARED_ACTIONS, sequence, shouldBeShared = true))
    }

    fun getGame() = state.game

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