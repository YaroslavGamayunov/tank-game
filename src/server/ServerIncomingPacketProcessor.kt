package server

interface ServerIncomingPacketProcessor {
    /**
     * @return If received Packet should be shared to other clients
     */
    fun onReceive(connection: ServerConnection, packet: ServerPacket) : Boolean


    /**
     * @return Packet that should be shared to other clients or null
     */
    fun onConnectionInterrupted(connection: ServerConnection) : ServerPacket?
}