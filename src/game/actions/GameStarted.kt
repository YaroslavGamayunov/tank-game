package game.actions

import game.Game

class GameStarted : GameAction() {
    override fun invoke(game: Game) {
        super.invoke(game)
        game.gameHasStarted = true;
    }

    override fun invoke(visitor: IActionVisitor) {
        visitor.onGameStarted(this)
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        return !game.gameHasStarted
    }
}