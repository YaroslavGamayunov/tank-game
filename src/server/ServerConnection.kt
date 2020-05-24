package server

import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class ServerConnection(socket: Socket) {
    private var socket: Socket = socket
    lateinit var inputStream: ObjectInputStream
    lateinit var outputStream: ObjectOutputStream

    var connectionCallback: ServerConnectionCallback? = null

    init {
        try {
            outputStream = ObjectOutputStream(socket.getOutputStream())
            inputStream = ObjectInputStream(socket.getInputStream())
            startInputLoop()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getAddress() = socket.inetAddress

    fun sendData(serverPacket: ServerPacket) {
        outputStream.writeObject(serverPacket)
        outputStream.flush()
    }

    private fun startInputLoop() {
        Thread {
            while (true) {
                try {
                    var inputPacket: ServerPacket = inputStream.readObject() as ServerPacket
                    connectionCallback?.onReceive(inputPacket)
                } catch (e: IOException) {
                    e.printStackTrace()
                    connectionCallback?.onConnectionInterrupted()
                    break
                }
            }
        }.start()
    }

}