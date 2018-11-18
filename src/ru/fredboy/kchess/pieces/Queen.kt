package ru.fredboy.kchess.pieces

import ru.fredboy.utils.Matrix2

class Queen(team: Int) : Piece(team, 5) {

    private fun possibleMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        var dx = 0
        var dy = 0
        try { dx = Math.abs(x2 - x1) / (x2 - x1) } catch (e: ArithmeticException) {}
        try { dy = Math.abs(y2 - y1) / (y2 - y1) } catch (e: ArithmeticException) {}

        if (y1 == y2) {
            var ix = x1 + dx
            while (ix != x2) {
                if (board[ix, y1] != null) return false
                ix += dx
            }
        }

        if (x1 == x2) {
            var iy = y1 + dy
            while (iy != y2) {
                if (board[x1, iy] != null) return false
                iy += dy
            }
        }

        if (x1 != x2 && y1 != y2) {
            var ix = x1 + dx
            var iy = y1 + dy
            while (ix != x2 && iy != y2) {
                if (board[ix, iy] != null) return false
                ix += dx
                iy += dy
            }
        }

        return true
    }

    override fun canMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (!(((x1 == x2 || y1 == y2) || Math.abs(x2 - x1) == Math.abs(y2 - y1)) && board[x2, y2] == null)) return false
        return possibleMove(board, x1, y1, x2, y2) && isValidMove(board, x1, y1, x2, y2)
    }

    override fun canKill(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (!(((x1 == x2 || y1 == y2) || Math.abs(x2 - x1) == Math.abs(y2 - y1)) && board[x2, y2] != null &&
                        board[x2, y2]!!.getTeam() != getTeam())) return false
        return possibleMove(board, x1, y1, x2, y2) && isValidMove(board, x1, y1, x2, y2)
    }

}