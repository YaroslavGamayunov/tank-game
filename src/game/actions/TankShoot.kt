package game.actions

import game.Game

class TankShoot(tankID: Int, val aimID : Int) : TankAction(tankID) {
    override fun invoke(game: Game) {
        super.invoke(game)
    }

    override fun invoke(visitor: IActionVisitor) {
        super.invoke(visitor)
    }

    override fun isCorrect(game: Game, sequence: GameActionSequence): Boolean {
        return super.isCorrect(game, sequence)
    }
}