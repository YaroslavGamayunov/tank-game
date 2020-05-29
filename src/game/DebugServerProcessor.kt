package game

class DebugServerProcessor : GameServerProcessor() {
    override fun createGameField(): GameFieldManager {
        return GameFieldManager(globalGameState, 1)
    }
}