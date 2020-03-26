package game

import game.actions.*

class LocalConnector : IGameServerConnector {
    lateinit var client: IGameClient
    val game : Game = Game()

    init {
         game.objects.add(GamePlayer(0))
    }

    override fun getGameCopy(): Game {
        return game
    }

    override fun getPlayerID(): Int {
        return 0
    }

    override fun runConnector(client: IGameClient) {
        this.client = client

        val gameStarted = GameActionSequence(-1)
        gameStarted.actions.add(GameStarted())
        executeActionSequence(gameStarted)
    }

    private fun executeActionSequence(sequence: GameActionSequence){
        if(sequence.playerID != client.owner.objectID) client.applyExternalActions(sequence)
    }


}