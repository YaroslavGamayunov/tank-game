package server

import java.io.*
import java.net.InetAddress
import java.net.ServerSocket

class Server(port: Int, private var packetProcessor: ServerIncomingPacketProcessor) : Thread() {

    private lateinit var serverSocket: ServerSocket
    var connectionSet: HashSet<ServerConnection> = HashSet()


    init {
        try {
            serverSocket = ServerSocket(port)
            println("Starting server on ${InetAddress.getLocalHost().hostAddress}")
            // TODO Add ability to control lifecycle
            start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /** Notifies all connections
     * @param except Server connection that will not be notified
     */
    private fun notifyAll(obj: ServerPacket, except: List<ServerConnection>) {
        for (connection in connectionSet) {
            if (connection in except) {
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

                        var packetsForSharing: List<BroadcastPacketWrapper> =
                                packetProcessor.onReceive(connection, serverPacket)

                        for (broadcastWrapper in packetsForSharing) {
                            notifyAll(broadcastWrapper.serverPacket, broadcastWrapper.connectionBlackList)
                        }
                    }

                    override fun onConnectionInterrupted() {
                        println("Client disconnected: $connection")
                        connectionSet.remove(connection)

                        var packetsForSharing: List<BroadcastPacketWrapper> =
                                packetProcessor.onConnectionInterrupted(connection)

                        for (broadcastWrapper in packetsForSharing) {
                            notifyAll(broadcastWrapper.serverPacket, broadcastWrapper.connectionBlackList)
                        }
                    }
                }
                connectionSet.add(connection)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}