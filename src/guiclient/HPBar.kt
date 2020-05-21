package guiclient

import game.units.GameUnit
import guiclient.tools.Vector3

open class HPBar<Context : IRenderingContext>(factory: IRendererFactory<Context>, val unit: GameUnit, position: Vector3) : VisualObjectCompositor<Context>(){
    override val renderer = factory.getHPRenderer(this);
    val percent : Double
    get() {
        return unit.health * 1.0 / unit.maxHealth
    }

    init {
        transform.position = position
    }
}