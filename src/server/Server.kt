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
            System.err.println("Starting server on ${InetAddress.getLocalHost().hostAddress}")
            // TODO Add ability to control lifecycle
            start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun notifyAll(packet: BroadcastPacket) {
        for (connection in connectionSet) {
            if (packet.whiteList.isNotEmpty() && !packet.whiteList.contains(connection)) {
                continue
            }
            if (packet.blackList.isNotEmpty() && packet.blackList.contains(connection)) {
                continue
            }
            connection.sendData(packet.serverPacket)
        }
    }


    override fun run() {
        while (true) {
            try {
                var socket = serverSocket.accept()
                var connection = ServerConnection(socket)

                connection.connectionCallback = object : ServerConnectionCallback {
                    override fun onReceive(serverPacket: ServerPacket) {
                        System.err.println("Server received object $serverPacket")

                        var packetsForSharing: List<BroadcastPacket> =
                                packetProcessor.onReceive(connection, serverPacket)

                        for (broadcastPacket in packetsForSharing) {
                            notifyAll(broadcastPacket)
                        }
                    }

                    override fun onConnectionInterrupted() {
                        System.err.println("Client disconnected: $connection")
                        connectionSet.remove(connection)

                        var packetsForSharing: List<BroadcastPacket> =
                                packetProcessor.onConnectionInterrupted(connection)

                        for (broadcastPacket in packetsForSharing) {
                            notifyAll(broadcastPacket)
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