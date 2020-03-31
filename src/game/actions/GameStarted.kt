package game.actions

import game.Game
import game.events.ActionEvent
import game.events.IGameEvent

class GameStarted : GameAction() {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onGameStarted(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) : IGameEvent?{
        game.assertGameHasNotStarted()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly) {
            return ActionEvent{
                game.gameHasStarted = true
            }
        }
        return null
    }
}