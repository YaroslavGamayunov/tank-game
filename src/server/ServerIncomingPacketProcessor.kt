package server

interface ServerIncomingPacketProcessor {
    /**
     * @return Packets that should be shared to other clients
     */
    fun onReceive(connection: ServerConnection, packet: ServerPacket): List<BroadcastPacket>


    /**
     * @return Packets that should be shared to other clients
     */
    fun onConnectionInterrupted(connection: ServerConnection): List<BroadcastPacket>
}