package game

import game.actions.GameActionSequence
import game.objects.GamePlayer
import game.tools.copySerializable
import server.*
import server.validation.PlayerActionsValidator
import server.validation.PacketPayloadValidator
import server.validation.ServerPacketValidationException
import server.validation.ServerPacketValidatorChain
import java.net.InetAddress
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class GameServerProcessor() : ServerIncomingPacketProcessor {
    private var playerIdByAddr = HashMap<InetAddress, String>()
    private var globalGameState = GameState().apply { game = Game() }
    private val validatorChain =
            ServerPacketValidatorChain(PacketPayloadValidator(), PlayerActionsValidator(globalGameState.game!!))
    private var fieldManager = GameFieldManager(globalGameState)


    override fun onReceive(connection: ServerConnection, packet: ServerPacket): List<BroadcastPacket> {
        try {
            validatorChain.validate(packet)
        } catch (e: ServerPacketValidationException) {
            e.printStackTrace()
            System.err.println("Packet validation FAILED: ${packet}")
            return listOf()
        }

        if (packet.type == PacketType.PLAYER_JOINED) {
            var player = packet.payload as Player
            playerIdByAddr[connection.getAddress()] = (packet.payload as Player).id
            globalGameState.players[player.id] = player

            player.localPlayerInstance = fieldManager.createLocalPlayer()

            var gameStatePacket = ServerPacket(PacketType.GAME_STATE, globalGameState.copySerializable())

            var packetList = arrayListOf<BroadcastPacket>()
            packetList.add(BroadcastPacket.withWhiteList(gameStatePacket, connection))

            var actionsForSharing = fieldManager.onPlayerConnected(player.localPlayerInstance!!)

            var actionsPacket = ServerPacket(PacketType.SHARED_ACTIONS, actionsForSharing)
            packetList.add(BroadcastPacket.withWhiteList(actionsPacket))

            return packetList
        } else if (packet.type == PacketType.SHARED_ACTIONS) {
            // todo create better validator for action sequence
            var actions = packet.payload as GameActionSequence
            var player = globalGameState.game?.getObjectByID(actions.playerID) as GamePlayer
            var actionsForSharing = fieldManager.onPlayerMoved(player, actions)

            var packetList = arrayListOf<BroadcastPacket>()
            var actionsPacket = ServerPacket(PacketType.SHARED_ACTIONS, actionsForSharing)
            packetList.add(BroadcastPacket.withWhiteList(actionsPacket))

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