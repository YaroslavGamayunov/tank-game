package game

import game.actions.IActionVisitor

interface IGameAction {
    operator fun invoke(visitor: IActionVisitor)
    operator fun invoke(game : Game)
    fun isCorrect(game: Game) : Boolean

}