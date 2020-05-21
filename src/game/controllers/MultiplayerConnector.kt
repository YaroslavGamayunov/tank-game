package game.controllers

import game.Game
import game.GameActionsListener
import game.actions.GameActionSequence
import game.actions.GameStarted
import game.actions.MoveBegin
import java.net.Socket

class ServerDataNotReceivedException(message: String) : Throwable(message)

class MultiplayerConnector(var socket: Socket, var playerName: String) : IGameServerConnector, GameActionsListener {

    lateinit var client: IGameClient

    fun waitForConnection() {
        while (true) {
            try {
                getGameCopy()
            } catch (e: ServerDataNotReceivedException) {
                Thread.sleep(100)
                continue
            }
            break
        }
    }

    override fun getGameCopy(): Game {
        var gameCopy = GameController.instance.getGame()

        if (gameCopy == null) {
            throw ServerDataNotReceivedException("Game instance hasn't been received by client yet")
        }

        return gameCopy
    }

//    override fun getGame(): Game {
//        var game = GameController.instance.gameModel?.state?.game
//
//        if (game == null) {
//            throw ServerDataNotReceivedException("Game instance hasn't been received by client yet")
//        }
//
//        return game
//    }

    override fun getPlayerID(): Int {
        var id: Int? = GameController.instance.getLocalPlayer()?.localPlayerInstance?.objectID

        if (id == null) {
            throw ServerDataNotReceivedException("Player ID hasn't been received by client yet")
        }

        return id
    }

    override fun runConnector(client: IGameClient) {
        this.client = client
        GameController.instance.addGameActionListener(this)
        GameController.instance.connectToServer(playerName, socket)
        // todo rebuld because it blocks the thread
        waitForConnection()
//        val gameStarted = GameActionSequence(-1)
//        gameStarted.actions.add(GameStarted())
//        executeActionSequence(gameStarted)
//
//        var game = GameController.instance.getGame(copy = false);
//        while (game?.gameIsOver == false) {
//            val newMove = GameActionSequence(-1)
//            newMove.actions.add(MoveBegin(0))
//            executeActionSequence(newMove)
//            executeActionSequence(client.makeYourMove())
//        }
    }

    // sends actions to server
    private fun executeActionSequence(sequence: GameActionSequence) {
        GameController.instance.onPlayerMoved(sequence, shouldUpdateModel = true)
        if (sequence.playerID != client.owner.objectID) client.applyExternalActions(sequence)
    }

    // receives actions from server
    override fun onReceive(sequence: GameActionSequence) {
        client.applyExternalActions(sequence)
        for (action in sequence.actions) {
            if (action is MoveBegin && action.playerID == getPlayerID()) {
                executeActionSequence(client.makeYourMove())
            }
        }
    }
}