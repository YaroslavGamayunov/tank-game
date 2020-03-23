package gui

import server.ServerConnection
import java.awt.Component
import java.awt.Dimension
import java.net.Socket
import javax.swing.*

class MainScreen : JFrame() {
    init {
        contentPane.layout = BoxLayout(contentPane, BoxLayout.Y_AXIS)


        var joinServerButton = JButton("Подключиться к серверу").apply {
            alignmentX = Component.CENTER_ALIGNMENT
            preferredSize = Dimension(200, 50)
        }

        var createHostButton = JButton("Стать хостом").apply {
            alignmentX = Component.CENTER_ALIGNMENT
            preferredSize = Dimension(200, 50)
        }

        joinServerButton.addActionListener {

            var connectionHostNameField = JTextField()
            var connectionPortField = JTextField()
            var playerNameField = JTextField()
            var serverJoinDialog = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(JLabel("Aдрес сервера"))
                add(connectionHostNameField)
                add(JLabel("Порт"))
                add(connectionPortField)
                add(JLabel("Имя игрока"))
                add(playerNameField)
            }

            var dialogResult = JOptionPane.showConfirmDialog(
                null, serverJoinDialog,
                "Подключение к серверу", JOptionPane.OK_CANCEL_OPTION
            )
            if (dialogResult == JOptionPane.OK_OPTION) {
                GameController.instance.connectToServer(
                    playerNameField.text,
                    connectionHostNameField.text,
                    connectionPortField.text.toInt()
                )
            }
        }


        createHostButton.addActionListener {
            var hostPortField = JTextField()
            var playerNameField = JTextField()
            var serverHostDialog = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(JLabel("Порт"))
                add(hostPortField)
                add(JLabel("Имя игрока"))
                add(playerNameField)
            }


            var dialogResult = JOptionPane.showConfirmDialog(
                null, serverHostDialog,
                "Cоздать сервер", JOptionPane.OK_CANCEL_OPTION
            )
            if (dialogResult == JOptionPane.OK_OPTION) {
                GameController.instance.hostServer(playerNameField.text, hostPortField.text.toInt())
            }
        }

        add(Box.createVerticalGlue())
        add(joinServerButton)
        add(createHostButton)
        add(Box.createVerticalGlue())

        contentPane.preferredSize = Dimension(600, 600)

        pack()
        setLocationRelativeTo(null)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        isVisible = true
    }
}