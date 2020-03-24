package gui

import game.GameState

interface GameStateListener {
    fun onGameStateChanged(state: GameState)
}