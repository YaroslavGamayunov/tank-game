package game.actions

import game.Game

interface IGameAction {
    operator fun invoke(visitor: IActionVisitor)
    operator fun invoke(game : Game)
    fun isCorrect(game: Game, sequence: GameActionSequence) : Boolean

}