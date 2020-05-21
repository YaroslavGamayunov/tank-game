package server

import java.io.Serializable

enum class PacketType {
    PLAYER_JOINED, PLAYER_LEFT, GAME_STATE, PLAYER_MOVED
}

data class ServerPacket(var type: PacketType, var payload: Any, var shouldBeShared: Boolean = false) : Serializable