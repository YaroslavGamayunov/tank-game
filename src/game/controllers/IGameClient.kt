package game.controllers

import game.Game
import game.objects.IGamePlayerProperty
import game.actions.GameActionSequence

/*
Created IGameClient must have server connector
 */
interface IGameClient : IGamePlayerProperty {
    val server: IGameServerConnector
    fun makeYourMove(): GameActionSequence
    fun applyExternalActions(sequence: GameActionSequence)
}