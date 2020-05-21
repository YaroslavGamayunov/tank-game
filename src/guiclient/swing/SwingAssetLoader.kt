package guiclient.swing

import java.awt.Image
import javax.imageio.ImageIO

class SwingAssetLoader {
    companion object {
        val instance = SwingAssetLoader()
    }

    init {

    }

    val images = hashMapOf<String, Image>()
    fun getImage(path : String) : Image{
        if(!images.containsKey(path)){
            images[path] = ImageIO.read(SwingRendererFactory::class.java.getResource(path))
        }

        return images[path]!!
    }
}