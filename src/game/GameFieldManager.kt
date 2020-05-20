package game

import game.actions.GameActionSequence
import game.actions.GameStarted
import game.actions.MoveBegin
import game.actions.ObjectsCreated
import game.objects.GameObject
import game.objects.GamePlayer
import game.tools.Orientation
import game.tools.Vector2
import game.units.Tank

class GameFieldManager(var game: Game, val minPlayersForStart: Int = 1) {
    var playersConnected = 0

    fun createLocalPlayer(): GamePlayer {
        var player = GamePlayer(game.vacantID())
        return player
    }

    fun onPlayerConnected(player: GamePlayer): GameActionSequence {
        System.err.println("Field manager detected connection")
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

        game.objects.add(tank)

        val addSeq = GameActionSequence(player.objectID)
        addSeq.addAction(ObjectsCreated(player))
        addSeq.addAction(ObjectsCreated(tank))
        if (playersConnected == minPlayersForStart) {
            addSeq.addAction(GameStarted())
            addSeq.addAction(MoveBegin(player.objectID))
        }
        return addSeq
    }
}