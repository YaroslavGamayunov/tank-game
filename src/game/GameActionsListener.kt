package game

import game.actions.GameActionSequence

interface GameActionsListener {
    fun onSequenceReceived(sequence: GameActionSequence)
    fun onGameReceived(game: Game)
}