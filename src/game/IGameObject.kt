package game
import java.io.Serializable

interface IGameObject : Serializable {
    fun linkIdentifiers(iIdentityProvider: IIdentityProvider)
}