import game.*
import game.controllers.CLIGameClient

fun main(){
    val connector = LocalSinglePlayerConnector()
    val client = CLIGameClient(connector)
    connector.runConnector(client)
}