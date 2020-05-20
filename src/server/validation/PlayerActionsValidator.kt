package server.validation

import game.Game
import game.actions.GameActionSequence
import game.actions.IllegalActionException
import game.actions.applyAllActions
import server.PacketType
import server.ServerPacket

class InvalidActionSequenceException(cause: Throwable? = null) :
        ServerPacketValidationException("Invalid action found during game sequence evaluating", cause)


class PlayerActionsValidator(var game: Game) : ServerPacketValidatorChainLink() {
    override fun invoke(packet: ServerPacket) {
        if (packet.type != PacketType.SHARED_ACTIONS) {
            return
        }
        var actionSequence = packet.payload as GameActionSequence
        var gameCopy = game.copy()

        if (gameCopy != null) {
            for (action in actionSequence.actions) {
                try {
                    applyAllActions(gameCopy, action)
                } catch (e: IllegalActionException) {
                    throw InvalidActionSequenceException(e)
                }
            }
        }
    }
}