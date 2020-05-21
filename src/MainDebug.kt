
fun main(){
    println("Enter CLI args")
    main(readLine()?.split(" ")?.toTypedArray() ?: arrayOf())
}