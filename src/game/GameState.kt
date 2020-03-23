package game

class GameState() {
    var players = HashMap<String, Player>()

    constructor(players: ArrayList<Player>) : this() {
        this.players.putAll(players.map { player -> player.id to player })
    }
}