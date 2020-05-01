package server.validation

import server.ServerPacket

abstract class ServerPacketValidatorChainLink {
    var nextLink: ServerPacketValidatorChainLink? = null
    abstract operator fun invoke(packet: ServerPacket)
}