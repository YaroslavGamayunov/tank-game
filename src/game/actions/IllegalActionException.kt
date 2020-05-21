package game.actions

import game.units.Tank
import kotlin.reflect.KClass

abstract class IllegalActionException( string: String? = null,throwable: Throwable? = null)
    : Throwable(string,throwable) {}

class GameDoubleStartingException() : IllegalActionException("Trying to start game twice")
class OtherMoveHasNotFinishedException() : IllegalActionException("Trying to start new move, while now it is other player's turn")
class FinishingInvalidMoveException() : IllegalActionException("Calling end of move, while move hasn't started")
class GameObjectTypeMismatchException(id: Int, typename: String) : IllegalActionException("Expected that [$id] is ${typename}, but it isn't")
class GameObjectPropertyMismatch(objectName : String, playerID : Int) : IllegalActionException("$objectName is not [Player $playerID]'s property")
class IllegalTankMoveException(message : String) : IllegalActionException(message)
class IllegalTankRotationException(message : String) : IllegalActionException(message)
class IllegalAttackException(message: String) : IllegalActionException(message)
class WrongIdException(objectID: Int) : IllegalActionException("Cannot find object with ID:$objectID")