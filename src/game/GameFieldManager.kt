package game

import game.actions.*
import game.objects.*
import game.tools.Orientation
import game.tools.Vector2
import game.units.Obstacle
import game.units.tanks.Tank

class GameFieldData(
    val playerCount : Int,
    val width : Int,
    val height : Int,
    val data : Array<String>,
    val lCount : Int= 2,
    val mCount : Int = 2,
    val hCount : Int = 2
){
}


class GameFieldManager(var gameState: GameState, val data: GameFieldData) {
    var playersConnected = 0
    val minPlayersForStart = data.playerCount
    var currentMovePlayerIndex = 0
    var gamePlayerIds = arrayListOf<Int>()

    private var game = gameState.game!!

    private var preliminaryActions = GameActionSequence(-1)
    fun createLocalPlayer(name: String): GamePlayer {
        return GamePlayer(game.vacantID(), name)
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
        obj.linkIdentifiers(game)
        preliminaryActions.addAction(ObjectsCreated(obj))
    }

    fun onPlayerConnected(player: GamePlayer): GameActionSequence {
        System.err.println("Field manager detected connection")
        game.objects.add(player)
        player.linkIdentifiers(game)
        playersConnected++
        preliminaryActions.addAction(ObjectsCreated(player))



            for(x in 0 until data.width){
                for(y in 0 until data.height){
                    val pos = Vector2(x - data.width/2,data.height/2 -y)
                    if(playersConnected == 1) {
                        if (data.data[y][x] == '#') {
                            addPrelimObject(Obstacle(game.vacantID(), pos))
                        }
                    }

                    if(data.data[y][x] == playersConnected.toString()[0]){
                        addPrelimObject(PlacementArea(game.vacantID(), player.objectID, pos.x, pos.x, pos.y,pos.y))
                    }
                }
            }



        addPrelimObject(LightPlacementSet(game.vacantID(), player.objectID, data.lCount, 'L'))
        addPrelimObject(HeavyPlacementSet(game.vacantID(), player.objectID, data.hCount, 'H'))
        addPrelimObject(MiddlePlacementSet(game.vacantID(), player.objectID, data.mCount, 'M'))

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