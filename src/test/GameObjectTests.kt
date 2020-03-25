package test

import game.GameObject
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.RuntimeException


fun testSerializingGameObjectID(){
    val obj = GameObject(0);
    val filename = "testSerializingGameObjectID.object"
    val fileOut = FileOutputStream(filename)
    val out = ObjectOutputStream(fileOut)
    out.writeObject(obj)
    out.close()
    fileOut.close()
    println("Object has been serialized")

    val fileIn = FileInputStream(filename)
    val input = ObjectInputStream(fileIn)

    val obj1 = input.readObject() as GameObject
    input.close()
    fileIn.close()

    if(obj1.objectID != obj.objectID){
        throw RuntimeException("Object was serialized improperly")
    }else{
        println("Everything's all right")
    }


}