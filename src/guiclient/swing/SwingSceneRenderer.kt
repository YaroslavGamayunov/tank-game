package guiclient.swing

import guiclient.IVisualObject
import guiclient.RenderingScene
import java.awt.Graphics

class SwingSceneRenderer(val scene: RenderingScene<SwingRenderingContext>) : SwingObjectRenderer {
    override fun render(graphics: Graphics, camera: SwingDefaultCamera) {
        renderSubtree(scene, graphics, camera)
    }

    private fun renderSubtree(visualObject : IVisualObject<SwingRenderingContext>, graphics: Graphics, camera: SwingDefaultCamera){
        for(child in visualObject.childs){
            val renderer = child.renderer as SwingObjectRenderer
            renderer.render(graphics, camera)
            renderSubtree(child, graphics, camera)
        }
    }

}