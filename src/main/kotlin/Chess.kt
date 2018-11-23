package ru.fredboy.kchess

import ru.fredboy.kchess.network.Data
import ru.fredboy.kchess.network.Networker
import ru.fredboy.kchess.pieces.*
import ru.fredboy.utils.GamePanel
import ru.fredboy.utils.Matrix2
import java.awt.Color
import java.awt.Graphics
import java.awt.Image
import java.awt.Point
import java.awt.event.MouseEvent
import javax.imageio.ImageIO
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

class Chess : GamePanel() {

    companion object {
        var check: Boolean = false
        var turn: Int = 0

        fun checkCheck(board: Matrix2<Piece?>, team: Int): Boolean {
            for (x in 0..7) for (y in 0..7) {
                if (board[x, y] != null && board[x, y]!!.getTeam() != team) {
                    for (xx in 0..7) for (yy in 0..7) {
                        if (board[x, y]!!.canKill(board, x, y, xx, yy) && board[xx, yy]!!.getType() == 4) {
                            return true
                        }
                    }
                }
            }
            return false
        }

        fun willHelp(board: Matrix2<Piece?>, x: Int, y: Int, move: Point, team: Int): Boolean {
            val tmp = board[move.x, move.y]
            board[move.x, move.y] = board[x, y]
            board[x, y] = null
            val tmpBool = checkCheck(board, team)
            board[x, y] = board[move.x, move.y]
            board[move.x, move.y] = tmp
            return !tmpBool
        }
    }

    private val cellSize = 64

    private val pieceImages: Matrix2<Image> = Matrix2(2, 6)
    private val board: Matrix2<Piece?> = Matrix2(8, 8)

    private var selectedPiece: Piece? = null
    private var selectedPieceX: Int = 0
    private var selectedPieceY: Int = 0

    private var boardX: Int = 0
    private var boardY: Int = 0

    val statusBar = JPanel()
    val status = JLabel()

    var networker: Networker? = null

    init {
        for (i in 0..5) {
            for (j in 0..1)
                pieceImages[j, i] = ImageIO.read(javaClass.classLoader.getResource(("pieces/" + i + "_" + j + ".png")))
        }
    }

    fun startPaintTimer() {
        paintTimer.start()
    }

    fun receiveData(data: Data) {
        board.copyFromMatrix(data.board)
        turn = data.turn
        check = checkCheck(board, turn % 2)
        if (checkMate(turn % 2)) JOptionPane.showMessageDialog(this, "Checkmate!")
    }

    fun exitMultiplayer() {
        val str = if (networker!!.type == Networker.Type.SERVER) "Client" else "Server"
        JOptionPane.showMessageDialog(this, "$str disconnected")
        networker = null
        newGame()
    }

    fun newGame() {
        board.clear()

        for (i in 0..7) {
            board[i, 6] = Pawn(0)
            board[i, 1] = Pawn(1)
        }

        board[0, 7] = Rook(0)
        board[1, 7] = Knight(0)
        board[2, 7] = Bishop(0)
        board[3, 7] = Queen(0)
        board[4, 7] = King(0)
        board[5, 7] = Bishop(0)
        board[6, 7] = Knight(0)
        board[7, 7] = Rook(0)

        board[0, 0] = Rook(1)
        board[1, 0] = Knight(1)
        board[2, 0] = Bishop(1)
        board[3, 0] = Queen(1)
        board[4, 0] = King(1)
        board[5, 0] = Bishop(1)
        board[6, 0] = Knight(1)
        board[7, 0] = Rook(1)

        check = false
        selectedPiece = null
        turn = 0

        if (checkNetworker(Networker.Type.SERVER)) networker!!.sendData(Data(board, turn))
    }

    private fun checkNetworker(type: Networker.Type?): Boolean {
        return if (networker != null) {
            (networker!!.type == type && networker!!.isConnected) || (type == null && !networker!!.isConnected)
        } else type == null
    }

    private fun drawChessBoard(g: Graphics) {
        for (i in 0..63) {
            val x = i % 8
            val y = i / 8
            if (selectedPiece != null && x == selectedPieceX && y == selectedPieceY) g.color = Color.ORANGE
            else if (selectedPiece != null && (x != selectedPieceX || y != selectedPieceY) &&
                    selectedPiece!!.canKill(board, selectedPieceX, selectedPieceY, x, y)) g.color = Color.RED
            else if (selectedPiece != null && (x != selectedPieceX || y != selectedPieceY) &&
                    selectedPiece!!.canMove(board, selectedPieceX, selectedPieceY, x, y)) g.color = Color.GREEN
            else if ((i + i / 8) % 2 == 0) g.color = Color.WHITE
            else g.color = Color.GRAY
            g.fillRect(boardX + x * cellSize, boardY + y * cellSize, cellSize, cellSize)
            if (board[x, y] != null) {
                g.drawImage(pieceImages[board[x, y]!!.getTeam(), board[x, y]!!.getType()],
                        boardX + x * cellSize, boardY + y * cellSize, null)
            }
        }
    }

    private fun selectPiece(x: Int, y: Int): Boolean {
        return if (board[x, y] != null && board[x, y]!!.getTeam() == turn % 2 &&
                (checkNetworker(null) ||
                        (checkNetworker(Networker.Type.SERVER) && turn % 2 == 0) ||
                        (checkNetworker(Networker.Type.CLIENT) && turn % 2 == 1))) {
            selectedPieceX = x
            selectedPieceY = y
            selectedPiece = board[selectedPieceX, selectedPieceY]
            true
        } else false
    }

    private fun checkMate(team: Int): Boolean {
        if (!check) return false
        for (x in 0..7) for (y in 0..7)
            if (board[x, y] != null && board[x, y]!!.getTeam() == team && board[x, y]!!.hasMoves(board, x, y))
                return false
        return true
    }

    private fun checkBoardBounds(x: Int, y: Int): Boolean {
        return ((x >= boardX && x <= boardX + (8 * cellSize)) && (y >= boardY && y <= boardY + (8 * cellSize)))
    }

    override fun paint(g: Graphics) {
        boardX = width / 2 - (cellSize * 8) / 2
        boardY = height / 2 - (cellSize * 8) / 2

        status.text = if (turn % 2 == 0) "White" else "Black"
        status.text += " | Turn: $turn"
        status.text += " | " + if (check) "Check" else "No check"

        g.color = Color.LIGHT_GRAY
        g.fillRect(0, 0, width, height)
        drawChessBoard(g)
    }

    override fun mousePressed(e: MouseEvent) {
        val tmpX = (e.x - boardX) / cellSize
        val tmpY = (e.y - boardY) / cellSize
        if (e.button == MouseEvent.BUTTON1 && checkBoardBounds(e.x, e.y)) {
            if (selectedPiece != null) {
                if (selectedPiece!!.canMove(board, selectedPieceX, selectedPieceY, tmpX, tmpY) ||
                        selectedPiece!!.canKill(board, selectedPieceX, selectedPieceY, tmpX, tmpY)) {
                    //castling...
                    if (board[tmpX, tmpY] != null && board[tmpX, tmpY]!!.getType() == 1 &&
                            board[tmpX, tmpY]!!.getTeam() == turn % 2) {
                        board[selectedPieceX + 2 * (Math.abs(tmpX - selectedPieceX) / (tmpX - selectedPieceX)), tmpY] =
                                board[selectedPieceX, selectedPieceY]
                        board[selectedPieceX, selectedPieceY] = null
                        board[selectedPieceX + (Math.abs(tmpX - selectedPieceX) / (tmpX - selectedPieceX)), tmpY] =
                                board[tmpX, tmpY]
                        board[tmpX, tmpY] = null
                        board[selectedPieceX + (Math.abs(tmpX - selectedPieceX) / (tmpX - selectedPieceX)), tmpY]!!.madeMove()
                    } else {
                        board[tmpX, tmpY] = selectedPiece
                        board[selectedPieceX, selectedPieceY] = null
                    }
                    selectedPiece!!.madeMove()
                    selectedPiece = null
                    turn++

                    if (!checkNetworker(null)) {
                        val b = Matrix2<Piece?>(8, 8)
                        b.copyFromMatrix(board)
                        networker!!.sendData(Data(b, turn))
                    }

                    check = checkCheck(board, turn % 2)
                    if (checkMate(turn % 2))
                        JOptionPane.showMessageDialog(this, "Checkmate!")

                } else selectPiece(tmpX, tmpY)
            } else selectPiece(tmpX, tmpY)
        } else if (e.button == MouseEvent.BUTTON3) selectedPiece = null
    }

}
