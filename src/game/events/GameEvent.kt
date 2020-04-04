package game.events

abstract class GameEvent : IGameEvent{
    override fun invoke(): IGameEvent? {
        return null
    }

    override fun invoke(visitor: IEventVisitor) {
        visitor.onUnknownEvent(this)
    }
}