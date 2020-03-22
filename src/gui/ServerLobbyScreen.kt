package gui

import game.GameState
import java.awt.Component
import java.awt.Font
import java.net.InetAddress
import javax.swing.*

class ServerLobbyScreen(hostName: String, port: Int) : JFrame(), GameStateListener {
    var connectedPlayersList = JList<String>().apply { Font("Apple Casual", Font.PLAIN, 40) }

    init {
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)
        add(JLabel("Ожидание игроков...").apply {
            font = Font("Apple Casual", Font.BOLD, 40);
            alignmentX = Component.CENTER_ALIGNMENT
        })

        add(JLabel("Адрес сервера: $hostName порт: $port").apply {
            font = Font("Apple Casual", Font.ITALIC, 18);
            alignmentX = Component.CENTER_ALIGNMENT
        })

        add(connectedPlayersList)

        pack()
        extendedState = MAXIMIZED_BOTH;


        setLocationRelativeTo(null);
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE

        isVisible = true
    }

    override fun onGameStateChanged(state: GameState) {
        for (player in state.players) {
            (connectedPlayersList.model as DefaultListModel).addElement(player)
        }
    }
}