package server

import java.io.*
import java.net.InetAddress
import java.net.ServerSocket

class Server(private val port: Int) : Thread() {

    private lateinit var serverSocket: ServerSocket
    var connectionList: ArrayList<ServerConnection> = ArrayList()


    init {
        try {
            serverSocket = ServerSocket(port);
            println("Starting server on ${InetAddress.getLocalHost().hostAddress}")
            start()
        } catch (e: IOException) {
            e.printStackTrace()
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
                    }
                }
                connectionList.add(connection)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}