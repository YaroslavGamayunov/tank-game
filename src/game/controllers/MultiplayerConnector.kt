package game.controllers

import game.Game
import game.GameModel
import server.ServerConnection
import server.ServerConnectionCallback
import server.ServerPacket
import java.net.Socket

class ServerDataNotReceivedException(message: String) : Throwable(message)

class MultiplayerConnector(socket: Socket, playerName: String) : IGameServerConnector {

    lateinit var client: IGameClient

    init {
        GameController.instance.connectToServer(playerName, socket)
    }

    override fun getGameCopy(): Game {
        var gameCopy = GameController.instance.gameModel?.getGameCopy()

        if (gameCopy == null) {
            throw ServerDataNotReceivedException("Game instance hasn't been received by client yet")
        }

        return gameCopy
    }

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