package game.controllers

import game.Game
import game.actions.*
import game.objects.GamePlayer
import game.tools.Orientation
import game.tools.Vector2
import game.units.*

class LocalSinglePlayerConnector : IGameServerConnector {
    lateinit var client: IGameClient
    val game : Game = Game()

    init {
         game.objects.add(GamePlayer(0))
         game.objects.add(Tank(1,0, Vector2(0, 2),8, Orientation.RIGHT, 4U, 2))
         game.objects.add(Tank(2,0, Vector2(2, 2),8, Orientation.LEFT, 4U, 2))
         game.objects.add(Obstacle(3,Vector2(1,0)))
         for(x in game.objects){
             x.linkIdentifiers(game)
         }
    }

    override fun getGameCopy(): Game {
        return game
    }

    override fun getPlayerID(): Int {
        return 0
    }

    override fun runConnector(factory: IGameClientFactory) {
        this.client = factory.createClient(this)

        val gameStarted = GameActionSequence(-1)
        gameStarted.actions.add(GameStarted())
        executeActionSequence(gameStarted)

        while(!game.gameIsOver){
            val newMove = GameActionSequence(-1)
            newMove.actions.add(MoveBegin(0))
            executeActionSequence(newMove)
            executeActionSequence(client.makeYourMove())
        }


    }

    private fun executeActionSequence(sequence: GameActionSequence){
        if(sequence.playerID != client.owner.objectID) client.applyExternalActions(sequence)
    }


}