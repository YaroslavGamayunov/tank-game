package server

interface ServerIncomingPacketProcessor {
    /**
     * @return Packets to be shared to other clients
     */
    fun onReceive(connection: ServerConnection, packet: ServerPacket): ArrayList<ServerPacket>

    /**
     * @return Packets to be shared to other clients
     */
    // TODO return list of commands to server
    fun onConnectionInterrupted(connection: ServerConnection): ArrayList<ServerPacket>
}