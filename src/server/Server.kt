package server

import java.io.*
import java.net.InetAddress
import java.net.ServerSocket

class Server(private val port: Int) : Thread() {

    private lateinit var serverSocket: ServerSocket
    var connectionSet: HashSet<ServerConnection> = HashSet()


    init {
        try {
            serverSocket = ServerSocket(port);
            println("Starting server on ${InetAddress.getLocalHost().hostAddress}")
            start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /** Notifies all connections
     * @param except Server connection that will not be notified
     */
    private fun notifyAll(obj: ServerObject, except: ServerConnection? = null) {
        for (connection in connectionSet) {
            if (connection == except) {
                continue
            }
            connection.sendData(obj)
        }
    }


    override fun run() {
        while (true) {
            try {
                var socket = serverSocket.accept()
                var connection = ServerConnection(socket)
                connection.connectionCallback = object : ServerConnectionCallback {
                    override fun onReceive(serverObject: ServerObject) {
                        println("Server received object $serverObject")
                        if (serverObject.shouldBeShared) {
                            notifyAll(serverObject, except = connection)
                        }
                    }

                    override fun onConnectionInterrupted() {
                        connectionSet.remove(connection)
                        println("Client disconnected: $connection")
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}