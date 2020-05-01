package guiclient.swing

import guiclient.IVisualObject
import guiclient.RenderingScene
import java.awt.Graphics

class SwingSceneRenderer(val scene: RenderingScene<SwingRenderingContext>) : SwingObjectRenderer {
    override fun render(graphics: Graphics) {
        renderSubtree(scene, graphics)
    }

    private fun renderSubtree(visualObject : IVisualObject<SwingRenderingContext>, graphics: Graphics){
        for(child in visualObject.childs){
            val renderer = child.renderer as SwingObjectRenderer
            renderer.render(graphics)
            renderSubtree(child, graphics)
        }
    }

}