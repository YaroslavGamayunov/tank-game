package game.actions

import game.Game
import game.GameAction

class GameStarted : GameAction() {
    override fun invoke(game: Game) {
        super.invoke(game)
        game.gameHasStarted = true;
    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onGameStarted(this)
    }
}