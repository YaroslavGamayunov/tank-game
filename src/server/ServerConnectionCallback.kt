package server

interface ServerConnectionCallback {
    fun onReceive(serverPacket: ServerPacket)
    fun onConnectionInterrupted()
}