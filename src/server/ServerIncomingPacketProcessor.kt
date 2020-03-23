package server

interface ServerIncomingPacketProcessor {
    /**
     * @return If received Packet should be shared to other clients
     */
    fun onReceive(connection: ServerConnection, packet: ServerPacket) : Boolean

    /**
     * @return Packet that should be shared to other clients or null
     */
    // TODO return list of commands to server
    fun onConnectionInterrupted(connection: ServerConnection) : ServerPacket?
}