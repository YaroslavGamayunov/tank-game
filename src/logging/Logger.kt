package logging


fun logInfo(caller: Any, message : String){
    println("[Log - Info][${caller.javaClass.kotlin.simpleName}] $message")
}

fun logWarning(caller: Any, message : String){
    println("[Log - Warning!!][${caller.javaClass.kotlin.simpleName}] $message")
}