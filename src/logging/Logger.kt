package logging


fun logInfo(caller: Any, message : String){
    println("[Log - Info][${caller.javaClass.kotlin.simpleName}] $message")
}