package server

import java.io.Serializable

enum class PacketType {
    PLAYER_JOINED, PLAYER_LEFT, MESSAGE, GAME_STATE
}

data class ServerPacket(var type: PacketType, var payload: Any, var shouldBeShared: Boolean = false) : Serializable