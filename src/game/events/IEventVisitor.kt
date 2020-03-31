package game.events

interface IEventVisitor {
    fun onUnknownEvent(event: IGameEvent)
    fun onUnitDestroyed(event : UnitDestroyed)
}