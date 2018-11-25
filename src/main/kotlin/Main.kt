package ru.fredboy.kchess

import com.google.common.net.InetAddresses
import ru.fredboy.kchess.network.Client
import ru.fredboy.kchess.network.Server
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.BevelBorder


private val mainFrame = JFrame("KChess")

val chess = Chess()

fun setupFrame() {
    mainFrame.minimumSize = Dimension(64 * 10, 64 * 10)
    mainFrame.size = mainFrame.minimumSize
    mainFrame.setLocationRelativeTo(null)
    mainFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE

    mainFrame.add(chess)

    val menuBar = JMenuBar()
    val gameMenu = JMenu("Game")

    val newItem = JMenuItem("New")
    newItem.addActionListener { chess.newGame() }

    val serverItem = JMenuItem("Start server")
    serverItem.addActionListener {
        if (chess.networker == null) chess.networker = Server(1969)
    }

    val clientItem = JMenuItem("Connect")
    clientItem.addActionListener {
        if (chess.networker == null) {
            val address = JOptionPane.showInputDialog(mainFrame, "Server address")
            if (address != null && (InetAddresses.isInetAddress(address) || address == "localhost")) {
                chess.networker = Client(address, 1969)
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Invalid address")
            }
        }
    }

    val disconnectItem = JMenuItem("Disconnect")
    disconnectItem.addActionListener {
        if (chess.networker != null) {
            chess.networker!!.closeSocket()
            chess.networker = null
        }
    }

    gameMenu.add(newItem)
    gameMenu.add(serverItem)
    gameMenu.add(clientItem)
    gameMenu.add(disconnectItem)
    menuBar.add(gameMenu)
    mainFrame.jMenuBar = menuBar

    chess.statusBar.border = BevelBorder(BevelBorder.LOWERED)
    mainFrame.add(chess.statusBar, BorderLayout.SOUTH)
    chess.statusBar.preferredSize = Dimension(mainFrame.width, 18)
    chess.statusBar.layout = BoxLayout(chess.statusBar, BoxLayout.X_AXIS)
    chess.status.horizontalAlignment = SwingConstants.LEFT
    chess.statusBar.add(chess.status)

    mainFrame.isVisible = true
}

fun main(args: Array<String>) {
    setupFrame()
    chess.newGame()
    chess.startPaintTimer()
}
