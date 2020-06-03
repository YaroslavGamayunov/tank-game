package game

import game.actions.GameActionSequence
import game.objects.GamePlayer
import game.tools.copySerializable
import server.*
import server.validation.PlayerActionsValidator
import server.validation.PacketPayloadValidator
import server.validation.ServerPacketValidationException
import server.validation.ServerPacketValidatorChain
import java.io.File
import java.io.InputStream
import java.net.InetAddress
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

open class GameServerProcessor(fieldDataFile : File? = null) : ServerIncomingPacketProcessor {
    private var playerIdByAddr = HashMap<InetAddress, String>()
    protected var globalGameState = GameState().apply { game = Game() }
    private val validatorChain =
            ServerPacketValidatorChain(PacketPayloadValidator(), PlayerActionsValidator(globalGameState.game!!))
    private var fieldManager = createGameField(fieldDataFile)

    protected open fun createGameField(file : File? = null) : GameFieldManager{
        var data : GameFieldData
        if(file != null){
            val inputStream: InputStream = file.inputStream()
            val lineList = mutableListOf<String>()
            inputStream.bufferedReader().forEachLine { lineList.add(it) }
            val array = lineList.slice(6 until lineList.size).toTypedArray()
            data = GameFieldData(lineList[0].toInt(), lineList[1].toInt(), lineList[2].toInt(), array, lineList[3].toInt(), lineList[4].toInt(), lineList[5].toInt())
        }else{
            data = GameFieldData(
                playerCount = 2,
                width = 10,
                height = 10,
                data = arrayOf(
                    "##########",
                    "#222#2222#",
                    "#2#####22#",
                    "#        #",
                    "# #  # # #",
                    "#     #  #",
                    "#  #     #",
                    "#11####11#",
                    "#11111111#",
                    "##########"

                ))
        }

        return GameFieldManager(globalGameState, data)
    }

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

            player.localPlayerInstance = fieldManager.createLocalPlayer(player.name)


            // weird trick
            var gameStateForSend = globalGameState.copySerializable() as GameState
            gameStateForSend.game = Game()


            var gameStatePacket = ServerPacket(PacketType.GAME_STATE, gameStateForSend)

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

            var receivedActionsPacket = ServerPacket(PacketType.SHARED_ACTIONS, actions)
            packetList.add(BroadcastPacket.withBlackList(receivedActionsPacket, connection))

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