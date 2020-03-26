package game

interface IGameServerConnector {
    fun getGameCopy() : Game
    fun getPlayerID() : Int
    fun runConnector(client: IGameClient)

}