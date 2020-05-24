package game.controllers

import game.Game
import game.tools.Orientation
import game.tools.Vector2
import game.actions.*
import game.events.IEventVisitor
import game.events.IGameEvent
import game.events.UnitDestroyed
import game.objects.GamePlayer
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import kotlin.system.exitProcess



open class CLIGameClient(server: IGameServerConnector) : GameClient(server), IActionVisitor, IEventVisitor {
    init{
        printLineToOutput("Starting CLI client.....")
        printLineToOutput("List of provided game objects")
        for(x in game.objects){
            printLineToOutput("[Object ${x.objectID}] ${x.toString()}")
        }
    }

    protected var lastSequence = GameActionSequence(owner.objectID)
    override fun makeYourMove() : GameActionSequence {

        printLineToOutput("Your turn, mister...")
        printLineToOutput("Type 'act [action_name] [args]' to make action")
        printLineToOutput("Type 'end' to make your turn and send it to other players")
        printLineToOutput("Type 'exit' to close application")

        lastSequence = GameActionSequence(owner.objectID)

        var waitForInput = true
        var action: IGameAction? = null
        while (waitForInput) {
            val command = readLineFromInput()
            val args = command.split(' ')

            try {
                when (args[0]) {
                    "end" -> {
                        waitForInput = false
                        action = MoveEnd(owner.objectID)
                    }

                    "act" -> {
                        when (args[1]) {
                            "tank" -> {
                                val tankID = args[2].toInt()
                                when (args[3]) {
                                    "move" -> {
                                        action = MoveTank(tankID,
                                                Vector2(args[4].toInt(), args[5].toInt())
                                        )
                                    }

                                    "rotate" -> {
                                        action = RotateTank(tankID, Orientation.valueOf(args[4]))
                                    }

                                    "shoot" -> {
                                        action = TankShoot(tankID, args[4].toInt())
                                    }
                                }
                            }
                        }
                    }

                    "exit" -> {
                        printLineToOutput("Goodbye player. Didn't expect you to be so scared of battle. It seems to me tha" +
                                "t you are not a real knight with brave heart. So sorry about it")
                        exitProcess(0)
                    }
                }
            } catch (ex: IndexOutOfBoundsException) {
                printErrorToOutput("Invalid command")
            } catch (ex: IllegalArgumentException) {
                printErrorToOutput("Invalid command")
            } catch (ex: NumberFormatException) {
                printErrorToOutput("Invalid command")
            }

            if (action == null) {
                printErrorToOutput("Unknown action")
            }
            if (action != null) {
                // TODO dont do this
                //var game: Game = GameController.instance.getGame() ?: return seq
                try {
                    applyMyAction(action, lastSequence)
                } catch (ex: IllegalActionException) {
                    printErrorToOutput(ex.message ?: "Some unknown illegal action")
                } finally {
                    action = null
                }

            }
        }
        return lastSequence
    }

    protected fun applyMyAction(action : IGameAction, seq : GameActionSequence){
        var event =  action(game)
        action(this)
        seq.actions.add(action)
        processEventResult(event)
    }

    private fun processEventResult(initialEvent: IGameEvent?) {
        var event = initialEvent
        while (event != null) {
            val next = event()
            gameStateChange()
            event(this)
            event = next
        }
    }


    override fun applyExternalActions(sequence: GameActionSequence) {
        for(action in sequence.actions){

            val event = action(game)
            gameStateChange()
            action(this)
            processEventResult(event)
        }
    }

    protected open fun gameStateChange(){

    }

    private fun readLineFromInput(): String {
        return readLine() ?: ""
    }

    private fun printLineToOutput(message: String) {
        println("[Client][Player ${owner.objectID}] $message")
    }

    private fun printErrorToOutput(message: String) {
        println("[ERROR] $message")
    }

    override fun onUnknownAction(action: IGameAction) {
        printLineToOutput("Unknown action {${action.toString()}}")
    }

    override fun onCreateObjects(action: ObjectsCreated) {
        printLineToOutput("Created object ${action.obj.toString()}")
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

    override fun onTankTurned(action: RotateTank) {
        printLineToOutput("Tank ${action.tankID} turned to ${action.orientation.name}")
    }

    override fun onTankShot(action: TankShoot) {
        printLineToOutput("Tank ${action.tankID} shot to ${action.aimID}")
    }

    override fun onUnknownEvent(event: IGameEvent) {
        printLineToOutput("On unknown event ${event.toString()}")
    }

    override fun onUnitDestroyed(event: UnitDestroyed) {
        printLineToOutput("Destroyed unit : ${event.unit.toString()}")
    }
}