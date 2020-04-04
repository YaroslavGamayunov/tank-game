package game.actions

import game.Game
import game.events.ActionEvent
import game.events.IGameEvent
import game.objects.GameObject
import game.tools.copySerializable


class ObjectsCreated(var obj : GameObject) : GameAction() {
    override fun invoke(visitor: IActionVisitor) {
        visitor.onCreateObjects(this)
    }

    override fun invoke(game: Game, checkCorrectnessOnly: Boolean): IGameEvent? {
        game.assertGameHasNotStarted()
        super.invoke(game, checkCorrectnessOnly)
        if(!checkCorrectnessOnly){
            obj = obj.copySerializable() as GameObject
            game.objects.add(obj)
            obj.linkIdentifiers(game)

        }
        return null
    }
}