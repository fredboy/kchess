package ru.fredboy.kchess

import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.BevelBorder


private val mainFrame = JFrame("KChess")
private var canvas = Canvas()

fun setupFrame() {
    mainFrame.setLocationRelativeTo(null)
    mainFrame.minimumSize = Dimension(64 * 10, 64 * 10)
    mainFrame.size = mainFrame.minimumSize
    mainFrame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    mainFrame.add(canvas)

    val menuBar = JMenuBar()
    val gameMenu = JMenu("Game")
    val newItem = JMenuItem("New")
    newItem.addActionListener { canvas.newGame() }
    gameMenu.add(newItem)
    menuBar.add(gameMenu)
    mainFrame.jMenuBar = menuBar

    canvas.statusBar.border = BevelBorder(BevelBorder.LOWERED)
    mainFrame.add(canvas.statusBar, BorderLayout.SOUTH)
    canvas.statusBar.preferredSize = Dimension(mainFrame.width, 18)
    canvas.statusBar.layout = BoxLayout(canvas.statusBar, BoxLayout.X_AXIS)
    canvas.status.horizontalAlignment = SwingConstants.LEFT
    canvas.statusBar.add(canvas.status)

    mainFrame.isVisible = true
}

fun main(args: Array<String>) {
    setupFrame()
    canvas.newGame()
    canvas.startPaintTimer()
}