package game

class GameState() {
    var players = ArrayList<Player>()

    constructor(players: ArrayList<Player>) : this() {
        this.players = players
    }
}