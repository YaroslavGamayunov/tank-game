package game.actions

import game.Game
import game.events.IGameEvent

interface IGameAction {
    operator fun invoke(visitor: IActionVisitor)
    operator fun invoke(game : Game, checkCorrectnessOnly : Boolean = false) : IGameEvent?
}