package game

import server.PacketType
import server.ServerConnection
import server.ServerIncomingPacketProcessor
import server.ServerPacket
import java.net.InetAddress

class GameServerProcessor : ServerIncomingPacketProcessor {
    private var playerIdByAddr = HashMap<InetAddress, String>()
    private var globalGameState = GameState()
    

    override fun onReceive(connection: ServerConnection, packet: ServerPacket): Boolean {

        if (packet.type == PacketType.PLAYER_JOINED) {
            var player = packet.payload as Player
            playerIdByAddr[connection.getAddress()] = (packet.payload as Player).id
            globalGameState.players[player.id] = player
            connection.sendData(ServerPacket(PacketType.GAME_STATE, globalGameState))
        }

        return packet.shouldBeShared
    }

    override fun onConnectionInterrupted(connection: ServerConnection): ServerPacket? {
        var disconnectedPlayerId: String? = playerIdByAddr[connection.getAddress()]

        if (disconnectedPlayerId != null) {
            playerIdByAddr.remove(connection.getAddress())
            globalGameState.players.remove(disconnectedPlayerId)
            return ServerPacket(PacketType.PLAYER_LEFT, disconnectedPlayerId, true)
        }
        return null
    }
}