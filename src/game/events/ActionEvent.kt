package game.events

typealias Operation = () -> Unit
class ActionEvent(val operation: Operation) : GameEvent() {
    override fun invoke(): IGameEvent? {
        operation()
        return null
    }
    override fun invoke(visitor: IEventVisitor) {}
    
}