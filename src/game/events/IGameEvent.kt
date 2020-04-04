package game.events

interface IGameEvent {
    operator fun invoke(visitor: IEventVisitor)
    operator fun invoke() : IGameEvent?
}