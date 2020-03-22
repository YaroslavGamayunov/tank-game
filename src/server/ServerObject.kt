package server

import java.io.Serializable

enum class PacketType {
    PLAYER_JOINED, PLAYER_LEFT, MESSAGE, GAME_STATE
}

data class ServerObject(var type: PacketType, var obj: Any, var shouldBeShared: Boolean = false) : Serializable