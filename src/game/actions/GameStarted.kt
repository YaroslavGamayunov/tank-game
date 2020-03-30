package game.actions

import game.Game

class GameStarted : GameAction() {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onGameStarted(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean) {
        game.assertGameHasNotStarted()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly) {
            game.gameHasStarted = true
        }
    }
}