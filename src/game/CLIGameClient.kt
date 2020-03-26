package game

import game.actions.*

class CLIGameClient(override val server: IGameServerConnector) : IGameClient, IActionVisitor {
    val game : Game = server.getGameCopy()
    override val owner : GamePlayer = game.getObjectByID(server.getPlayerID()) as GamePlayer

    override fun makeYourMove() : GameActionSequence{
        printLineToOutput("Your turn, mister...")
        printLineToOutput("Type 'act [action_name] [args]' to make action")
        printLineToOutput("Type 'end' to make your turn and send it to other players")

        val seq = GameActionSequence(owner.objectID)

        var waitForInput = true
        var action : IGameAction? = null
        while(waitForInput){
            val command = readLineFromInput()
            val args = command.split(' ')
            if(args.isEmpty()) continue
            when(args[0]){
                "end" ->{
                    waitForInput = false
                    action = MoveEnd(owner.objectID)
                }
            }


            if(action!=null){
                action(game)
                action(this)
                seq.actions.add(action)
                action = null
            }
        }
        return seq
    }

    override fun applyExternalActions(sequence: GameActionSequence) {
        printLineToOutput("Action sequence has come owner = [Player ${sequence.playerID}]")
        for(action in sequence.actions){
            action(game)
            action(this)
        }
        printLineToOutput("End of action sequence")

    }

    private fun readLineFromInput() : String{
        return readLine() ?: ""
    }

    private fun printLineToOutput(message : String){
        println("[Client][Player ${owner.objectID}] $message" )
    }

    override fun onUnknownAction(action: IGameAction) {
        printLineToOutput("Unknown action {${action.toString()}}")
    }

    override fun onGameStarted(action: GameStarted) {
        printLineToOutput("Game started !!!! Are you ready for a real battle? xD :)")
    }

    override fun onMoveStarted(action: MoveBegin) {
        printLineToOutput("Waiting for [Player ${action.playerID}] to make his move")
    }

    override fun onMoveEnd(action: MoveEnd) {
        printLineToOutput("Current move is over - [Player ${action.playerID}] made his move")
    }
}