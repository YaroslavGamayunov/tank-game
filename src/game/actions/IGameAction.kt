package game.actions

import game.Game

interface IGameAction {
    operator fun invoke(visitor: IActionVisitor)
    operator fun invoke(game : Game, checkCorrectnessOnly : Boolean = false)
}