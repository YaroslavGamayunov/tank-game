package game.objects
import game.tools.*

interface IPositionProvider : IIdentityProvider{
    fun getObjects(position : Vector2) : List<IGameLocated>
    fun getSolid(position: Vector2) : IGameLocated?
    fun isValidCell(position: Vector2) : Boolean
}