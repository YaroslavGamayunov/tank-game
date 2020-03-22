package server

interface ServerConnectionCallback {
    fun onReceive(serverObject: ServerObject)
}