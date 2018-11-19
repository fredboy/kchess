package ru.fredboy.kchess

import com.google.common.net.InetAddresses
import ru.fredboy.network.Client
import ru.fredboy.network.Server
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.BevelBorder


private val mainFrame = JFrame("KChess")
private var chess = Chess()

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
        try{
            val t = Thread {JOptionPane.showMessageDialog(mainFrame, "Waiting for client...")}
            t.start()
            chess.networker = Server(chess, 1969)
            t.interrupt()
        } catch(e: Exception) {
            JOptionPane.showMessageDialog(mainFrame, "Error: ${e.message}")
        }
        JOptionPane.showMessageDialog(mainFrame, "Client connected")
        chess.newGame()
    }

    val clientItem = JMenuItem("Connect")
    clientItem.addActionListener {
        val address =  JOptionPane.showInputDialog(mainFrame, "Server address")
        if (InetAddresses.isInetAddress(address)) {
            try{
                chess.networker = Client(chess, address, 1969)
            } catch(e: Exception) {
                JOptionPane.showMessageDialog(mainFrame, "Error: ${e.message}")
            }
            JOptionPane.showMessageDialog(mainFrame, "Connected")
            chess.newGame()
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Invalid address")
        }
    }

    gameMenu.add(newItem)
    gameMenu.add(serverItem)
    gameMenu.add(clientItem)
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
