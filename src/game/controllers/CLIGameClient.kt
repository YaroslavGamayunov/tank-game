package game.controllers

import game.Game
import game.tools.Orientation
import game.tools.Vector2
import game.actions.*
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import kotlin.system.exitProcess

class CLIGameClient(override val server: IGameServerConnector) : IGameClient, IActionVisitor {
    val game : Game = server.getGameCopy()
    override val owner : GamePlayer = game.getObjectByID(server.getPlayerID()) as GamePlayer

    init{
        printLineToOutput("Starting CLI client.....")
        printLineToOutput("List of provided game objects")
        for(x in game.objects){
            printLineToOutput("[Object ${x.objectID}] ${x.toString()}")
        }
    }

    override fun makeYourMove() : GameActionSequence {
        printLineToOutput("Your turn, mister...")
        printLineToOutput("Type 'act [action_name] [args]' to make action")
        printLineToOutput("Type 'end' to make your turn and send it to other players")
        printLineToOutput("Type 'exit' to close application")

        val seq = GameActionSequence(owner.objectID)

        var waitForInput = true
        var action : IGameAction? = null
        while(waitForInput){
            val command = readLineFromInput()
            val args = command.split(' ')

            try{
            when(args[0]){
                "end" ->{
                    waitForInput = false
                    action = MoveEnd(owner.objectID)
                }

                "act"->{
                    when(args[1]){
                        "tank"->{
                            when(args[2]){
                                "move"->{
                                    action = MoveTank(args[3].toInt(),
                                        Vector2(args[4].toInt(), args[5].toInt())
                                    )
                                }

                                "turn"->{
                                    action = TurnTank(args[3].toInt(), Orientation.valueOf(args[4]))
                                }
                            }
                        }
                    }
                }

                "exit"->{
                    printLineToOutput("Goodbye player. Didn't expect you to be so scared of battle. It seems to me tha" +
                            "t you are not a real knight with brave heart. So sorry about it")
                    exitProcess(0)
                }
            }
            }
            catch (ex : IndexOutOfBoundsException){
               printErrorToOutput("Invalid command")
            }
            catch (ex : IllegalArgumentException){
                printErrorToOutput("Invalid command")
            }
            catch (ex :NumberFormatException){
                printErrorToOutput("Invalid command")
            }



            if(action!=null){
                if(action.isCorrect(game,seq)) {
                    action(game)
                    action(this)
                    seq.actions.add(action)
                    action = null
                }else{
                    printErrorToOutput("Incorrect action")
                }
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

    private fun printErrorToOutput(message: String){
        println("[ERROR] $message")
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

    override fun onTankMove(action: MoveTank) {
        printLineToOutput("Tank ${action.tankID} moved to ${action.newPosition}")
    }

    override fun onTankTurned(action: TurnTank) {
        printLineToOutput("Tank ${action.tankID} turned to ${action.orientation.name}")
    }
}