package game.actions

import game.Game
import game.events.IGameEvent

open class GameWarAction : GameAction() {
    override fun invoke(game: Game, checkCorrectnessOnly: Boolean): IGameEvent? {
        super.invoke(game, checkCorrectnessOnly)
        game.assertWarStage()
        return null
    }
}