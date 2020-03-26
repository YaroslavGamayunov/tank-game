package game
/*
Created IGameClient must have server connector
 */
interface IGameClient : IGamePlayerProperty {
    val server : IGameServerConnector
    fun makeYourMove() : GameActionSequence
    fun applyExternalActions(sequence: GameActionSequence)

}