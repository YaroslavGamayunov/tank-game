package game.tools

import game.objects.IPositionProvider

fun IPositionProvider.isVacantHalfInterval(begin : Vector2, end: Vector2,orientation: Orientation) : Boolean{
    var pos = begin
    while(pos != end){
        if(this.getSolid(pos) != null)return false
        pos += orientation.direction
    }
    return true
}