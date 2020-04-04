package game.tools

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable


fun Serializable.copySerializable() : Serializable{
    val baos = ByteArrayOutputStream()
    val ous = ObjectOutputStream(baos)
    ous.writeObject(this)
    ous.close()
    val bais = ByteArrayInputStream(baos.toByteArray())
    val ois = ObjectInputStream(bais)
    return ois.readObject() as Serializable
}