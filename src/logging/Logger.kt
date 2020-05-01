package logging


fun logInfo(caller: Any, message : String){
    print("[Log - Info][${caller.javaClass.kotlin.simpleName}] $message")
}