package server

interface ServerIncomingPacketProcessor {
    /**
     * @return Packets that should be shared to other clients
     */
    fun onReceive(connection: ServerConnection, packet: ServerPacket): List<BroadcastPacketWrapper>


    /**
     * @return Packets that should be shared to other clients
     */
    fun onConnectionInterrupted(connection: ServerConnection): List<BroadcastPacketWrapper>
}