package guiclient

/**
 * Object, which renders sprite within context
 * Scaling: supposed that width of window is N units, the width of sprite will be transform.scale.x units,
 * height is calculated as initial height / initial width * width in units.
 * Number of units in window is camera implementation responsibility
 */
open class Sprite<Context : IRenderingContext>(factory: IRendererFactory<Context>, path : String) : VisualObjectCompositor<Context>(){
    override val renderer: IVisualObjectRenderer<Context> = factory.getSpriteRenderer(this, path)
}