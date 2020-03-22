package server

import java.io.Serializable

enum class PacketType {
    CONNECTION_DATA, MESSAGE, GAME_STATE
}

data class ServerObject(var type: PacketType, var obj: Any) : Serializable