package server

/** This is a class which representing additional options for sending ServerPacket to all clients
 *
 */
data class BroadcastPacketWrapper(var serverPacket: ServerPacket, var connectionBlackList: ArrayList<ServerConnection>)