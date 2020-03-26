package game

import game.actions.*

class CLIGameClient(override val server: IGameServerConnector) : IGameClient, IActionVisitor {
    val game : Game = server.getGameCopy()
    override val owner : GamePlayer = game.getObjectByID(server.getPlayerID()) as GamePlayer

    override fun makeYourMove() : GameActionSequence{
        return GameActionSequence(0)
    }

    override fun applyExternalActions(sequence: GameActionSequence) {
        printLine("Action sequence has come owner = [Player ${sequence.playerID}]")
        for(action in sequence.actions){
            action(game)
            action(this)
        }
        printLine("End of action sequence")
    }



    private fun printLine(message : String){
        println("[Client][Player ${owner.objectID}] $message" )
    }

    override fun onUnknownAction(action: IGameAction) {
        printLine("Unknown action {${action.toString()}}")
    }

    override fun onGameStarted(action: GameStarted) {
        printLine("Game started !!!! Are you ready for a real battle? xD :)")
    }
}