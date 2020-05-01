package game

import java.io.Serializable
import java.math.BigInteger

enum class GameStatus {
    LOBBY, ACTIVE
}

class GameState() : Serializable {
    lateinit var status: GameStatus
    var players = HashMap<String, Player>()
    var game = Game()

    constructor(players: ArrayList<Player>, gameStatus: GameStatus = GameStatus.LOBBY) : this() {
        this.players.putAll(players.map { player -> player.id to player })
        status = gameStatus
    }
}