package game.controllers

import game.Game
import java.net.Socket

class ServerDataNotReceivedException(message: String) : Throwable(message)

class MultiplayerConnector(socket: Socket, playerName: String) : IGameServerConnector {

    lateinit var client: IGameClient

    init {
        GameController.instance.connectToServer(playerName, socket)
    }

    override fun getGameCopy(): Game {
        var gameCopy = GameController.instance.getGameCopy()

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
        var id: Int? = GameController.instance.gameModel?.localPlayer?.localPlayerInstance?.objectID

        if (id == null) {
            throw ServerDataNotReceivedException("Player ID hasn't been received by client yet")
        }

        return id
    }

    override fun runConnector(client: IGameClient) {
        this.client = client
    }
}