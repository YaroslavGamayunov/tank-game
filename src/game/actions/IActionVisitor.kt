package game.actions

interface IActionVisitor{
    fun onUnknownAction(action: IGameAction)
    fun onGameStarted(action: GameStarted)
    fun onMoveStarted(action: MoveBegin)
    fun onMoveEnd(action: MoveEnd)
    fun onTankMove(action:MoveTank)
    fun onTankTurned(action:RotateTank)
    fun onTankShot(action: TankShoot)
}