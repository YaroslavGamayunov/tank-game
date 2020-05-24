package server

/** This is a class which represents additional options for sending ServerPacket to all clients
 *
 */
class BroadcastPacket(var serverPacket: ServerPacket,
                      var blackList: List<ServerConnection>,
                      var whiteList: List<ServerConnection>) {
    companion object {
        fun withBlackList(serverPacket: ServerPacket, blackList: List<ServerConnection>): BroadcastPacket {
            return BroadcastPacket(serverPacket, blackList, arrayListOf())
        }

        fun withWhiteList(serverPacket: ServerPacket, whiteList: List<ServerConnection>): BroadcastPacket {
            return BroadcastPacket(serverPacket, arrayListOf(), whiteList)
        }

        fun withBlackList(serverPacket: ServerPacket, vararg blackList: ServerConnection): BroadcastPacket {
            return BroadcastPacket(serverPacket, blackList.toList(), arrayListOf())
        }

        fun withWhiteList(serverPacket: ServerPacket, vararg whiteList: ServerConnection): BroadcastPacket {
            return BroadcastPacket(serverPacket, arrayListOf(), whiteList.toList())
        }
    }
}
