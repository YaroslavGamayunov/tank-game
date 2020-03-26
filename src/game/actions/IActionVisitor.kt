package game.actions
import game.*

interface IActionVisitor{
    fun onUnknownAction(action: IGameAction)
    fun onGameStarted(action: GameStarted)
    fun onMoveStarted(action: MoveBegin)
    fun onMoveEnd(action: MoveEnd)
}