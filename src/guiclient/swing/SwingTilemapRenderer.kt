package guiclient.swing

import game.tools.Vector2
import guiclient.Sprite
import guiclient.tools.Vector3
import java.awt.Graphics
import java.awt.Image

class SwingTilemapRenderer(sprite : Sprite<SwingRenderingContext>, image : Image) : SwingSpriteRenderer(sprite, image){
    override fun render(graphics: Graphics, camera: SwingDefaultCamera) {
        val begin = camera.toUnitCoords(Vector2(0,0))
        val end = camera.toUnitCoords(camera.canvasSize)

        var begX = begin.x.toInt()
        var endX = end.x.toInt()
        var begY = begin.y.toInt()
        var endY = end.y.toInt()

        if(begX > endX) begX = endX.also { endY = begX }
        if(begY > endY) begY = endY.also { endY = begY }

        for(x in (begX-1)..(endX+1)){
            for(y in (begY-1)..(endY+1)){
                renderAt(graphics, camera, Vector3(x.toDouble(),y.toDouble()))
            }
        }

    }

}