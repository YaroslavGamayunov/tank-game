package server

import java.io.*
import java.net.InetAddress
import java.net.ServerSocket

class Server(port: Int) : Thread() {

    private lateinit var serverSocket: ServerSocket
    var connectionSet: HashSet<ServerConnection> = HashSet()


    init {
        try {
            serverSocket = ServerSocket(port)
            println("Starting server on ${InetAddress.getLocalHost().hostAddress}")
            start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /** Notifies all connections
     * @param except Server connection that will not be notified
     */
    private fun notifyAll(obj: ServerPacket, except: ServerConnection? = null) {
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
                    override fun onReceive(serverPacket: ServerPacket) {
                        println("Server received object $serverPacket")
                        if (serverPacket.shouldBeShared) {
                            notifyAll(serverPacket, except = connection)
                        }
                    }

                    override fun onConnectionInterrupted() {
                        connectionSet.remove(connection)
                        println("Client disconnected: $connection")
                    }
                }
                connectionSet.add(connection)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}