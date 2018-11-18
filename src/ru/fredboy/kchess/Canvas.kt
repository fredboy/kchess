package ru.fredboy.kchess

import ru.fredboy.kchess.pieces.*
import ru.fredboy.utils.Matrix2
import java.awt.*
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.Timer

class Canvas : JPanel(), MouseListener {

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

    private val timer = Timer(10) { repaint() }
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

    init {
        addMouseListener(this)
        for (i in 0..5) {
            for (j in 0..1) pieceImages[j, i] = ImageIO.read(File("res/${i}_$j.png"))
        }
    }

    fun startPaintTimer() {
        timer.start()
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

    override fun paint(g: Graphics) {
        boardX = width / 2 - (cellSize * 8) / 2
        boardY = height / 2 - (cellSize * 8) / 2

        status.text = if (turn % 2 == 0) "White" else "Black"

        g.color = Color.LIGHT_GRAY
        drawChessBoard(g)
    }

    private fun checkMate(team: Int): Boolean {
        if (!check) return false
        var kingX = 0
        var kingY = 0
        while (board[kingX, kingY] == null ||
                (board[kingX, kingY]!!.getType() != 4 && board[kingX, kingY]!!.getTeam() == team)) {
            kingX++
            if (kingX >= board.width) {
                kingX = 0
                kingY++
                if (kingY >= board.height) return false
            }
        }
        if (board[kingX, kingY]!!.hasMoves(board, kingX, kingY)) return false
        for (x in 0..7) for (y in 0..7) {
            if (board[x, y] != null && board[x, y]!!.getTeam() == team) {
                val moves: ArrayList<Point> = board[x, y]!!.getMoves(board, x, y)
                if (moves.isNotEmpty()) {
                    for (move in moves) if (willHelp(board, x, y, move, team)) return false
                }
            }
        }
        return true
    }

    override fun mouseReleased(e: MouseEvent) {}

    override fun mouseEntered(e: MouseEvent) {}

    override fun mouseClicked(e: MouseEvent) {
        val tmpX = (e.x - boardX) / cellSize
        val tmpY = (e.y - boardY) / cellSize
        if (e.button == MouseEvent.BUTTON1 && checkBoardBounds(e.x, e.y)) {
            if (selectedPiece == null && board[tmpX, tmpY] != null && board[tmpX, tmpY]!!.getTeam() == turn % 2) {
                selectedPieceX = tmpX
                selectedPieceY = tmpY
                selectedPiece = board[selectedPieceX, selectedPieceY]
            } else if (selectedPiece != null) {
                if (selectedPiece!!.canMove(board, selectedPieceX, selectedPieceY, tmpX, tmpY) ||
                        selectedPiece!!.canKill(board, selectedPieceX, selectedPieceY, tmpX, tmpY)) {
                    //castling...
                    if (board[tmpX, tmpY] != null && board[tmpX, tmpY]!!.getType() == 1) {
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
                    check = checkCheck(board, (turn + 1) % 2)
                    if (checkMate((turn + 1) % 2))
                        JOptionPane.showMessageDialog(this, "Checkmate!")
                    selectedPiece!!.madeMove()
                    selectedPiece = null
                    turn++
                }
            }
        } else if (e.button == MouseEvent.BUTTON3)
            selectedPiece = null
    }

    override fun mouseExited(e: MouseEvent) {}

    override fun mousePressed(e: MouseEvent) {}

    private fun checkBoardBounds(x: Int, y: Int): Boolean {
        return ((x >= boardX && x <= boardX + (8 * cellSize)) && (y >= boardY && y <= boardY + (8 * cellSize)))
    }

}