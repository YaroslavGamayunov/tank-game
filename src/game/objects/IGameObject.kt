package game.objects
import java.io.Serializable

interface IGameObject : Serializable {
    fun linkIdentifiers(iIdentityProvider: IIdentityProvider)
    fun cleanOnNewMove()
}