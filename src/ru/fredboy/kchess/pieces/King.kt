package ru.fredboy.kchess.pieces

import ru.fredboy.utils.Matrix2

class King(team: Int) : Piece(team, 4) {

    private fun isInvalidMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        val tmp: Piece? = board[x2, y2]
        board[x2, y2] = board[x1, y1]
        board[x1, y1] = null
        var res = false
        for (x in 0..7) for (y in 0..7) {
            if (board[x, y] != null &&
                    ((board[x, y]!!.getType() == 4 && board[x, y]!!.canMove(board, x, y, x2, y2)) ||
                            board[x, y]!!.canKill(board, x, y, x2, y2))) {
                res = true
            }
        }
        board[x1, y1] = board[x2, y2]
        board[x2, y2] = tmp
        return res
    }

    private fun castlingPossible(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (board[x2, y2] != null && board[x2, y2]!!.getType() == 1 && board[x2, y2]!!.getTeam() == getTeam() &&
                !board[x2, y2]!!.isMoved() && !isMoved()&&
                !isInvalidMove(board, x1, y1, x2, y2) && !isInvalidMove(board, x1, y1, x1, y1)) {
            val dx = Math.abs(x2 - x1) / (x2 - x1)
            var x = x1 + dx
            while (x != x2) {
                if (board[x, y1] != null || isInvalidMove(board, x1, y1, x, y2)) return false
                x += dx
            }
        } else return false
        return true
    }

    override fun canMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return (((((x1 == x2 && Math.abs(y2 - y1) == 1) || (y1 == y2 && Math.abs(x2 - x1) == 1)) ||
                (Math.abs(x2 - x1) == Math.abs(y2 - y1)) &&
                Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 1) &&
                board[x2, y2] == null &&
                !isInvalidMove(board, x1, y1, x2, y2)) ||
                castlingPossible(board, x1, y1, x2, y2))
    }

    override fun canKill(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return ((((x1 == x2 && Math.abs(y2 - y1) == 1) || (y1 == y2 && Math.abs(x2 - x1) == 1)) ||
                (Math.abs(x2 - x1) == Math.abs(y2 - y1)) &&
                Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 1) &&
                board[x2, y2] != null &&
                board[x2, y2]!!.getTeam() != getTeam() &&
                !isInvalidMove(board, x1, y1, x2, y2))
    }

}