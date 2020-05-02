package server

import java.io.Serializable

enum class PacketType {
    PLAYER_JOINED, PLAYER_LEFT, GAME_STATE, SHARED_ACTIONS
}

data class ServerPacket(var type: PacketType, var payload: Any, var shouldBeShared: Boolean = false) : Serializable