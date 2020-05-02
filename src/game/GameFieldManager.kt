package game

import game.actions.GameActionSequence
import game.actions.ObjectsCreated
import game.objects.GameObject
import game.objects.GamePlayer
import game.tools.Orientation
import game.tools.Vector2
import game.units.Tank

class GameFieldManager(var game: Game) {
    var playersConnected = 0

    fun createLocalPlayer(): GamePlayer {
        var player = GamePlayer(game.vacantID())
        game.objects.add(player)
        return player
    }

    fun onPlayerConnected(player: GamePlayer): GameActionSequence {
        game.objects.add(player)
        playersConnected++

        var tankPosition: Vector2

        if (playersConnected == 1) {
            tankPosition = Vector2(0, 0)
        } else {
            tankPosition = Vector2(10, 0)
        }

        val tank = Tank(
                objectID = game.vacantID(),
                position = tankPosition,
                damage = 2U,
                moveDistance = 4,
                health = 4,
                playerID = player.objectID,
                orientation = Orientation.UP)

        val addSeq = GameActionSequence(-1)
        addSeq.addAction(ObjectsCreated(player))
        addSeq.addAction(ObjectsCreated(tank))
        return addSeq
    }
}