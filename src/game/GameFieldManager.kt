package game

import game.actions.*
import game.objects.GameObject
import game.objects.GamePlayer
import game.tools.Orientation
import game.tools.Vector2
import game.units.Obstacle
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

    fun addPrelimObject(obj : GameObject){
        game.objects.add(obj)
        preliminaryActions.addAction(ObjectsCreated(obj))
    }

    fun onPlayerConnected(player: GamePlayer): GameActionSequence {
        System.err.println("Field manager detected connection")
        game.objects.add(player)
        player.linkIdentifiers(game)
        playersConnected++
        preliminaryActions.addAction(ObjectsCreated(player))

        if(playersConnected == 1){

            addPrelimObject(Obstacle(game.vacantID(), Vector2(-5, 4)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-4, 3)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-4, 4)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-4, 6)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-3, 6)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-2, 3)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-2, 6)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-2, 7)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(-1, 3)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(0, 5)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(1, 3)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(2, 3)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(2, 6)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(2, 7)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(3, 3)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(3, 4)))
            addPrelimObject(Obstacle(game.vacantID(), Vector2(3, 7)))
        }

        for (ypos in 1..5) {
            var tankPosition: Vector2

            if (playersConnected == 1) {
                tankPosition = Vector2(-2 + ypos, 0)
            } else {
                tankPosition = Vector2(-2 + ypos, 10)
            }

            val tank = Tank(
                objectID = game.vacantID(),
                position = tankPosition,
                damage = 2U,
                moveDistance = 4,
                health = 4,
                playerID = player.objectID,
                orientation = if (playersConnected == 1) Orientation.UP else Orientation.DOWN
            )


            game.objects.add(tank)
            tank.linkIdentifiers(game)


            preliminaryActions.addAction(ObjectsCreated(tank))
        }
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