package game

import game.actions.GameActionSequence

interface GameActionsListener {
    fun onReceive(sequence: GameActionSequence)
}