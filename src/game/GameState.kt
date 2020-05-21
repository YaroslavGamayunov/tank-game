package game

import java.io.Serializable

enum class GameStatus {
    LOBBY, ACTIVE
}

class GameState() : Serializable {
    var status: GameStatus = GameStatus.LOBBY
    var players = HashMap<String, Player>()
    var game: Game? = null

    constructor(players: ArrayList<Player>, gameStatus: GameStatus = GameStatus.LOBBY) : this() {
        this.players.putAll(players.map { player -> player.id to player })
        status = gameStatus
    }
}