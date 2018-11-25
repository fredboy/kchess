package ru.fredboy.kchess.pieces

import ru.fredboy.kchess.chess
import ru.fredboy.utils.Matrix2

class Bishop(team: Int) : Piece(team, 3) {

    private fun possibleMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        var dx = 0
        var dy = 0
        try { dx = Math.abs(x2 - x1) / (x2 - x1) } catch (e: ArithmeticException) {}
        try { dy = Math.abs(y2 - y1) / (y2 - y1) } catch (e: ArithmeticException) {}

        var ix = x1 + dx
        var iy = y1 + dy
        while (ix != x2 && iy != y2) {
            if (board[ix, iy] != null) return false
            ix += dx
            iy += dy
        }

        return true
    }

    override fun canMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (!(Math.abs(x2 - x1) == Math.abs(y2 - y1) && chess.board[x2, y2] == null)) return false
        return possibleMove(chess.board, x1, y1, x2, y2) && isValidMove(x1, y1, x2, y2)
    }

    override fun canKill(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (!(Math.abs(x2 - x1) == Math.abs(y2 - y1) && chess.board[x2, y2] != null &&
                        chess.board[x2, y2]!!.team != team)) return false
        return possibleMove(chess.board, x1, y1, x2, y2) && isValidMove(x1, y1, x2, y2)
    }

}