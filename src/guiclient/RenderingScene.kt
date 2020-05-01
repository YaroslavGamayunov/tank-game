package guiclient

import logging.logInfo

open class RenderingScene<Context : IRenderingContext>
    : VisualObjectCompositor<Context>() {
    override lateinit var renderer: IVisualObjectRenderer<Context>
}