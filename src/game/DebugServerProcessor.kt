package game

import java.io.File

class DebugServerProcessor : GameServerProcessor() {
    override fun createGameField(file : File?): GameFieldManager {
        return GameFieldManager(globalGameState, GameFieldData(
            playerCount = 1,
            width = 10,
            height = 10,
            data = arrayOf(
                "##########",
                "#222#2222#",
                "#2#####22#",
                "#        #",
                "# #  # # #",
                "#     #  #",
                "#  #     #",
                "#11####11#",
                "#11111111#",
                "##########"

            )
        ))
    }
}