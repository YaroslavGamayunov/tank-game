package game.controllers

import guiclient.GUIClient
import guiclient.RenderingScene
import guiclient.swing.SwingRenderingContext
import guiclient.swing.SwingSceneRenderer

interface IGameClientFactory {
    fun createClient(connector: IGameServerConnector) : IGameClient
}

class CLIClientFactory : IGameClientFactory{
    override fun createClient(connector: IGameServerConnector): IGameClient {
        return CLIGameClient(connector)
    }
}

class SwingClientFactory : IGameClientFactory{
    override fun createClient(connector: IGameServerConnector): IGameClient {
        val swingContext1 = SwingRenderingContext()
        val scene1 = RenderingScene<SwingRenderingContext>()
        scene1.renderer = SwingSceneRenderer(scene1)
        return GUIClient<SwingRenderingContext>(connector, scene1, swingContext1)
    }
}