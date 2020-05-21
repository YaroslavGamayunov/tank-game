package game.controllers

import game.Game
import game.actions.GameActionSequence
import game.actions.GameStarted
import game.actions.MoveBegin
import game.actions.ObjectsCreated
import game.objects.GamePlayer
import game.tools.Orientation
import game.tools.Vector2
import game.units.Obstacle
import game.units.Tank

class LocalMultiplayerServer {
    private val game = Game()
    private val players = arrayListOf<Pair<GamePlayer, LocalMultiplayerConnector>>()

    init{
        game.friendlyFireAllowed = false
        game.objects.add(Obstacle(game.vacantID(), Vector2(1, 1)))
        game.objects.add(Obstacle(game.vacantID(), Vector2(1, 2)))
        game.objects.add(Obstacle(game.vacantID(), Vector2(1, 3)))
    }

    fun gameCopy() = game.copy()

    fun connectPlayer(connector: LocalMultiplayerConnector, tankPosition: Vector2) : Int{
        val addSeq = GameActionSequence(-1)

        //Creating player object
        val player = GamePlayer(game.vacantID())
        game.objects.add(player)
        addSeq.addAction(ObjectsCreated(player))
        println("[Local Multiplayer Server] Player connected giving id = ${player.objectID}")

        //Creating his tank
        val tank = Tank(
            objectID = game.vacantID(),
            position = tankPosition,
            damage = 2U,
            moveDistance = 4,
            health = 4,
            playerID = player.objectID,
            orientation = Orientation.UP)


        game.objects.add(tank)
        addSeq.addAction(ObjectsCreated(tank))

        //Sending info to already connected players
        executeActionSequence(addSeq)

        //Connecting new player
        players.add(player to connector)
        return player.objectID
    }

    fun startGame(){
        val gameStarted = GameActionSequence(-1)
        gameStarted.actions.add(GameStarted())
        executeActionSequence(gameStarted)

        while (true){
            for((player,connector) in players){
                val newMove = GameActionSequence(-1)
                newMove.actions.add(MoveBegin(player.objectID))
                executeActionSequence(newMove)
                executeActionSequence(connector.yourTurn())
            }
        }
    }

    fun executeActionSequence(actionSequence: GameActionSequence){
        for((player,connector) in players){
            connector.pushSequence(actionSequence)
        }
    }


}

class LocalMultiplayerConnector(val server: LocalMultiplayerServer, tankPosition: Vector2) : IGameServerConnector{
    private val playerID = server.connectPlayer(this, tankPosition)
    private lateinit var client : IGameClient
    override fun getGameCopy() : Game {
        return server.gameCopy()
    }

    override fun getPlayerID(): Int = playerID

    override fun runConnector(factory: IGameClientFactory) {
        this.client = factory.createClient(this)
    }

    fun pushSequence(actionSequence: GameActionSequence){
        if(actionSequence.playerID != playerID){
            client.applyExternalActions(actionSequence)
        }
    }

    fun yourTurn() : GameActionSequence{
        return client.makeYourMove()
    }

}