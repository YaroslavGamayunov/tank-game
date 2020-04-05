package server.validation

import server.ServerPacket

class ServerPacketValidatorChain {
    private var startLink: ServerPacketValidatorChainLink


    fun validate(packet: ServerPacket) {
        var currentLink: ServerPacketValidatorChainLink? = startLink

        while (currentLink != null) {
            currentLink(packet)
            currentLink = currentLink.nextLink
        }
    }

    constructor(vararg links: ServerPacketValidatorChainLink) {
        assert(links.isNotEmpty())
        startLink = links[0];
        var currentLink: ServerPacketValidatorChainLink? = startLink

        for (i in 1 until links.size) {
            currentLink?.nextLink = links[i]
            currentLink = currentLink?.nextLink
        }
    }

    constructor(links: ArrayList<ServerPacketValidatorChainLink>) : this(*(links.toTypedArray()))
}