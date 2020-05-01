package game

import game.objects.GamePlayer
import server.*
import server.validation.PlayerActionsValidator
import server.validation.PacketPayloadValidator
import server.validation.ServerPacketValidationException
import server.validation.ServerPacketValidatorChain
import java.net.InetAddress
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GameServerProcessor() : ServerIncomingPacketProcessor {
    private val validatorChain = ServerPacketValidatorChain(PacketPayloadValidator(), PlayerActionsValidator())
    private var playerIdByAddr = HashMap<InetAddress, String>()
    private var globalGameState = GameState().apply { game = Game() }
    private var fieldManager = GameFieldManager(globalGameState.game!!)


    override fun onReceive(connection: ServerConnection, packet: ServerPacket): List<BroadcastPacketWrapper> {
        try {
            validatorChain.validate(packet)
        } catch (e: ServerPacketValidationException) {
            return listOf()
        }

        if (packet.type == PacketType.PLAYER_JOINED) {
            var player = packet.payload as Player
            playerIdByAddr[connection.getAddress()] = (packet.payload as Player).id
            globalGameState.players[player.id] = player

            player.localPlayerInstance = fieldManager.createLocalPlayer()

            fieldManager.onPlayerConnected(player.localPlayerInstance!!)

            // TODO: Fix, Error can be here
            connection.sendData(ServerPacket(PacketType.GAME_STATE, globalGameState))
        }

        if (packet.shouldBeShared) {
            var blackList = ArrayList(listOf(connection))
            return listOf(BroadcastPacketWrapper(packet, blackList))
        }
        return listOf()
    }

    override fun onConnectionInterrupted(connection: ServerConnection): List<BroadcastPacketWrapper> {
        var disconnectedPlayerId: String? = playerIdByAddr[connection.getAddress()]

        if (disconnectedPlayerId != null) {
            playerIdByAddr.remove(connection.getAddress())
            globalGameState.players.remove(disconnectedPlayerId)

            var packet = ServerPacket(PacketType.PLAYER_LEFT, disconnectedPlayerId, true)
            var blackList = ArrayList(listOf(connection))

            return listOf(BroadcastPacketWrapper(packet, blackList))
        }
        return listOf()
    }
}