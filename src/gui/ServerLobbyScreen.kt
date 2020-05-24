package gui

import game.GameState
import java.awt.Component
import java.awt.Font
import javax.swing.*

class ServerLobbyScreen(hostName: String, port: Int) : JFrame(), GameStateListener {
    private var connectedPlayersListModel =
            DefaultListModel<String>().apply { Font("Apple Casual", Font.PLAIN, 40) }

    init {
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        add(JLabel("Ожидание игроков...").apply {
            font = Font("Apple Casual", Font.BOLD, 40)
            alignmentX = Component.CENTER_ALIGNMENT
        })

        add(JLabel("Адрес сервера: $hostName").apply {
            font = Font("Apple Casual", Font.ITALIC, 18)
            alignmentX = Component.CENTER_ALIGNMENT
        })

        add(JLabel("Порт: $port").apply {
            font = Font("Apple Casual", Font.ITALIC, 18)
            alignmentX = Component.CENTER_ALIGNMENT
        })

        var connectedPlayersList = JList(connectedPlayersListModel)
        add(connectedPlayersList)

        pack()
        extendedState = MAXIMIZED_BOTH


        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        isVisible = false
    }

    override fun onGameStateChanged(state: GameState) {
        connectedPlayersListModel.clear()
        connectedPlayersListModel.addAll(state.players.map { (playerId, player) -> player.name })
    }
}