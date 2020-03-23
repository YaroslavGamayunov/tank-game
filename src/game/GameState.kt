package game

enum class GameStatus {
    LOBBY, ACTIVE
}

class GameState() {
    lateinit var status: GameStatus
    var players = HashMap<String, Player>()

    constructor(players: ArrayList<Player>, gameStatus: GameStatus = GameStatus.LOBBY) : this() {
        this.players.putAll(players.map { player -> player.id to player })
        status = gameStatus
    }
}