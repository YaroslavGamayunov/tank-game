package server.validation

import game.GameState
import game.Player
import game.actions.GameActionSequence
import server.PacketType
import server.ServerPacket
import kotlin.reflect.KClass

private val PAYLOAD_MAP = mapOf<PacketType, KClass<*>>(
        PacketType.PLAYER_JOINED to Player::class,
        PacketType.PLAYER_LEFT to Player::class,
        PacketType.GAME_STATE to GameState::class,
        PacketType.PLAYER_MOVED to GameActionSequence::class)

class PacketPayloadMismatchException(packet: ServerPacket, throwable: Throwable? = null) :
        ServerPacketValidationException("Server packet payload mismatch: found ${packet.payload::class}, " +
                "but expected ${PAYLOAD_MAP[packet.type]}", throwable)

class PacketPayloadValidator : ServerPacketValidatorChainLink() {
    override fun invoke(packet: ServerPacket) {
        var expectedClass = PAYLOAD_MAP[packet.type]
        if (expectedClass != null) {
            if (!expectedClass.isInstance(packet.payload)) {
                throw PacketPayloadMismatchException(packet)
            }
        }
    }
}