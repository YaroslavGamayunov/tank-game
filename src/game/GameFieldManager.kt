package game

import game.actions.*
import game.objects.GameObject
import game.objects.GamePlayer
import game.tools.Orientation
import game.tools.Vector2
import game.units.Tank

class GameFieldManager(var gameState: GameState, val minPlayersForStart: Int = 2) {
    var playersConnected = 0

    var currentMovePlayerIndex = 0
    var gamePlayerIds = arrayListOf<Int>()

    private var game = gameState.game!!

    private var preliminaryActions = GameActionSequence(-1)
    fun createLocalPlayer(): GamePlayer {
        var player = GamePlayer(game.vacantID())
        return player
    }

    fun changeCurrentMovePlayer(): Int {
        currentMovePlayerIndex = (currentMovePlayerIndex + 1) % gamePlayerIds.size
        var currentMovePlayer = gamePlayerIds[currentMovePlayerIndex]
        var action = MoveBegin(currentMovePlayer)
        applyAllActions(game, action)
        return gamePlayerIds[currentMovePlayerIndex]
    }

    fun onPlayerConnected(player: GamePlayer): GameActionSequence {
        System.err.println("Field manager detected connection")
        game.objects.add(player)
        player.linkIdentifiers(game)
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
        tank.linkIdentifiers(game)

        preliminaryActions.addAction(ObjectsCreated(player))
        preliminaryActions.addAction(ObjectsCreated(tank))
        if (playersConnected == minPlayersForStart) {
            preliminaryActions.addAction(GameStarted())
            for ((_, player) in gameState.players) {
                player.localPlayerInstance?.objectID?.let { gamePlayerIds.add(it) }
            }
            preliminaryActions.addAction(MoveBegin(changeCurrentMovePlayer()))
            return preliminaryActions
        }
        return GameActionSequence(-1)
    }

    // Receives actions sent from server and returns actions that all players should receive
    fun onPlayerMoved(player: GamePlayer, actionSequence: GameActionSequence): GameActionSequence {
        for (action in actionSequence.actions) {
            applyAllActions(game, action)
        }

        var currentMovePlayer = changeCurrentMovePlayer()
        val responseSequence = GameActionSequence(currentMovePlayer)
        responseSequence.addAction(MoveBegin(currentMovePlayer))
        return responseSequence
    }
}