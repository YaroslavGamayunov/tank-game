package game.controllers

import game.Game
import game.actions.IActionVisitor
import game.events.IEventVisitor
import game.objects.GamePlayer

open abstract class GameClient(override val server: IGameServerConnector) : IGameClient {
    val game : Game = server.getGameCopy()
    override val owner: GamePlayer = game.getObjectByID(server.getPlayerID()) as GamePlayer
}