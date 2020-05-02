package game

import server.*
import server.validation.PlayerActionsValidator
import server.validation.PacketPayloadValidator
import server.validation.ServerPacketValidationException
import server.validation.ServerPacketValidatorChain
import java.net.InetAddress
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GameServerProcessor() : ServerIncomingPacketProcessor {
    private val validatorChain = ServerPacketValidatorChain(PacketPayloadValidator(), PlayerActionsValidator())
    private var playerIdByAddr = HashMap<InetAddress, String>()
    private var globalGameState = GameState().apply { game = Game() }
    private var fieldManager = GameFieldManager(globalGameState.game!!)


    override fun onReceive(connection: ServerConnection, packet: ServerPacket): List<BroadcastPacket> {
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

            var actionsForSharing = fieldManager.onPlayerConnected(player.localPlayerInstance!!)

            var packetList = arrayListOf<BroadcastPacket>()

            var gameStatePacket = ServerPacket(PacketType.GAME_STATE, globalGameState)
            packetList.add(BroadcastPacket.withWhiteList(gameStatePacket, connection))


            var actionsPacket = ServerPacket(PacketType.SHARED_ACTIONS, actionsForSharing)
            packetList.add(BroadcastPacket.withBlackList(actionsPacket, connection))

            return packetList
        }

        if (packet.shouldBeShared) {
            return listOf(BroadcastPacket.withBlackList(packet, connection))
        }
        return listOf()
    }

    override fun onConnectionInterrupted(connection: ServerConnection): List<BroadcastPacket> {
        var disconnectedPlayerId: String? = playerIdByAddr[connection.getAddress()]

        if (disconnectedPlayerId != null) {
            playerIdByAddr.remove(connection.getAddress())
            globalGameState.players.remove(disconnectedPlayerId)

            var packet = ServerPacket(PacketType.PLAYER_LEFT, disconnectedPlayerId, true)

            return listOf(BroadcastPacket.withBlackList(packet, connection))
        }
        return listOf()
    }
}