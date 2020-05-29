package game.actions

import game.Game
import game.events.IGameEvent
import game.events.UnitDestroyed
import game.objects.*
import game.tools.Orientation
import game.tools.Vector2
import game.units.tanks.Tank
import game.tools.*
import game.units.GameUnit

fun IGameAction.isCorrect(game: Game) = try {
    invoke(game); true
} catch (ex: IllegalActionException) {
    false
}

fun Game.assertGameHasNotStarted() = if (gameHasStarted) throw GameDoubleStartingException() else null

fun Game.assertCanStartNewMove() = if (waitingForPlayerToMove) throw OtherMoveHasNotFinishedException() else null

fun Game.assertCanFinishMove() = if (!waitingForPlayerToMove) throw FinishingInvalidMoveException() else null

fun IIdentityProvider.getTank(tankID: Int): Tank = getObjectByID(tankID) as? Tank
        ?: throw GameObjectTypeMismatchException(tankID, Tank::class.simpleName ?: "")

fun IIdentityProvider.getUnit(unitID: Int): GameUnit = getObjectByID(unitID) as? GameUnit
        ?: throw GameObjectTypeMismatchException(unitID, GameUnit::class.simpleName ?: "")

fun IGamePlayerProperty.assertProperty(playerID: Int) = if (owner.objectID != playerID)
    throw GameObjectPropertyMismatch(toString(), playerID) else null

fun Tank.assertMovingToAnotherCell(pos: Vector2) = if (position == pos)
    throw IllegalTankMoveException("Zero move is not allowed") else null

fun Tank.assertOrientation(orientation: Orientation) = if (this.orientation != orientation)
    throw IllegalTankMoveException("Moving only ${this.orientation.name} is currently allowed") else null

fun Tank.assertCanMoveThatFar(move: Vector2) = if (tempSteps + move.manhattanAbs > moveDistance)
    throw IllegalTankMoveException("Only $moveDistance steps allowed for [$objectID] tank") else null

fun Tank.assertMoveThroughEmptySpace(positionProvider: IPositionProvider, dest: Vector2) =
        if (!positionProvider.isVacantHalfInterval(position + orientation.direction, dest + orientation.direction, orientation))
            throw IllegalTankMoveException("Trying to move through objects")
        else null

fun Tank.assertCanRotate(orient: Orientation) {
    if (orient == orientation) throw IllegalTankRotationException("Idle turn is not allowed")
    if (tempTurns + 1 > maxTurns) throw  IllegalTankRotationException("Only $maxTurns rotations per turn is allowed")
}

fun Game.assertAttackAllowed(attacker: GameUnit, victim: GameUnit) {
    if (!friendlyFireAllowed) {
        if (attacker.owner == victim.owner) throw IllegalAttackException("Friendly fire is not allowed")
    } else {
        if (attacker == victim) throw  IllegalAttackException("Unit cannot shoot itself")
    }
}

fun Game.assertTankCanShootPosition(tank: Tank, pos: Vector2) = if (!tank.canShootPoint(pos, this))
    throw IllegalAttackException("Tank ${tank.objectID} can't shoot ${pos.toString()}") else null

fun Game.applyDamage(victim: GameUnit, attacker: GameUnit, damage: UInt): IGameEvent? {
    victim.applyDamage(damage, attacker)
    if (victim.health == 0) {
        return UnitDestroyed(victim, this)
    }

    return null
}

fun Vector2.assertAndGetOrientation() = orientation ?: throw IllegalTankMoveException("Cannot find direction")

fun Game.assertWarStage() = if(isTankPlacementStage) throw WrongStageException("War hasn't begun") else null

fun Game.assertPlacementStage() = if(!isTankPlacementStage) throw WrongStageException("War has begun") else null

fun Tank.assertHasShots() = if (tempShots + 1 > maxShots)
    throw IllegalAttackException("Tank $objectID has only $maxShots shots") else null

fun Game.assertCanPlace(pos: Vector2, player : GamePlayer){
    if(getSolid(pos) != null) throw PlacementException("Cell is taken by unit ${getSolid(pos)?.toString()}")
    if(objects.count{it is PlacementArea && it.inside(pos) && it.owner == player} == 0){
        throw PlacementException("Cannot place tank in this area")
    }
}


fun applyAllActions(game: Game, action: IGameAction) {
    val event = action(game)
    action(null)
    var currentEvent = event
    while (currentEvent != null) {
        val next = currentEvent()
        currentEvent(null)
        currentEvent = next
    }
}