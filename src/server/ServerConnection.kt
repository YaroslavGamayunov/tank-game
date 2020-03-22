package server

import java.io.EOFException
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

    fun sendData(serverObject: ServerObject) {
        outputStream.writeObject(serverObject)
        outputStream.flush()
    }

    private fun startInputLoop() {
        Thread {
            while (true) {
                try {
                    var inputObject: ServerObject = inputStream.readObject() as ServerObject
                    connectionCallback?.onReceive(inputObject)
                } catch (e: IOException) {
                    e.printStackTrace()
                    connectionCallback?.onConnectionInterrupted()
                    break
                }
            }
        }.start()
    }

}