package game.events

typealias EOperation = () -> IGameEvent?

class ComplexActionEvent(val eOperation: EOperation) : GameEvent() {
    override fun invoke(visitor: IEventVisitor?) {}
    override fun invoke(): IGameEvent? {
        return eOperation()
    }
}