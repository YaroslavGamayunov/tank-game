package server.validation

import game.actions.GameActionSequence
import game.actions.IllegalActionException
import server.PacketType
import server.ServerPacket

class InvalidActionSequenceException(cause: Throwable? = null) :
        ServerPacketValidationException("Invalid action found during game sequence evaluating", cause)


class PlayerActionsValidator : ServerPacketValidatorChainLink() {
    override fun invoke(packet: ServerPacket) {
        if (packet.type != PacketType.SHARED_ACTIONS) {
            return
        }
        var actionSequence = packet.payload as GameActionSequence
        var gameCopy = GameController.instance.getGameCopy()

        if (gameCopy != null) {
            for (action in actionSequence.actions) {
                try {
                    action(gameCopy)
                } catch (e: IllegalActionException) {
                    throw InvalidActionSequenceException(e)
                }
            }
        }
    }
}