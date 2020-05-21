package game.controllers

import game.Game

interface IGameServerConnector {
    fun getGameCopy() : Game
    fun getPlayerID() : Int
    fun runConnector(clientFactory: IGameClientFactory)

}