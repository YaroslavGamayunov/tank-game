import game.*
import test.*

fun main(){
    val connector = LocalConnector()
    val client = CLIGameClient(connector)
    connector.runConnector(client)
}