package guiclient

/**
 * Object, which renders sprite within context
 * Scaling: supposed that width of window is N units, the width of sprite will be transform.scale.x units,
 * height is calculated as initial height / initial width * width in units.
 * Number of units in window is camera implementation responsibility
 */
open class Sprite<Context : IRenderingContext>(factory: IRendererFactory<Context>, path : String, val isTilemap : Boolean = false) : VisualObjectCompositor<Context>(){
    override val renderer: IVisualObjectRenderer<Context> = if (isTilemap) factory.getTilemapRenderer(this, path)
                                                            else factory.getSpriteRenderer(this, path)
}