package server

import game.actions.GameActionSequence

fun printPayload(packet: ServerPacket) {
    if (packet.type == PacketType.SHARED_ACTIONS) {
        System.err.println("Content of packet:")
        var actionSequence = packet.payload as GameActionSequence
        for (action in actionSequence.actions) {
            System.err.println(action)
        }
        if (actionSequence.actions.isEmpty()) {
            System.err.println("it is empty :(")
        }
    }
}
