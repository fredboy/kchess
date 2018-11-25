package ru.fredboy.kchess.pieces

import ru.fredboy.kchess.chess

class King(team: Int) : Piece(team, 4) {

    private fun isInvalidMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        val tmp: Piece? = chess.board[x2, y2]
        chess.board[x2, y2] = chess.board[x1, y1]
        chess.board[x1, y1] = null
        var res = false
        for (x in 0..7) for (y in 0..7) {
            if (chess.board[x, y] != null &&
                    ((chess.board[x, y]!!.type == 4 && chess.board[x, y]!!.canMove(x, y, x2, y2)) ||
                            chess.board[x, y]!!.canKill(x, y, x2, y2))) {
                res = true
            }
        }
        chess.board[x1, y1] = chess.board[x2, y2]
        chess.board[x2, y2] = tmp
        return res
    }

    private fun castlingPossible(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        if (chess.board[x2, y2] != null && chess.board[x2, y2]!!.type == 1 && chess.board[x2, y2]!!.team == team &&
                !chess.board[x2, y2]!!.moved && !moved &&
                !isInvalidMove(x1, y1, x2, y2) && !isInvalidMove(x1, y1, x1, y1)) {
            val dx = Math.abs(x2 - x1) / (x2 - x1)
            var x = x1 + dx
            while (x != x2) {
                if (chess.board[x, y1] != null || isInvalidMove(x1, y1, x, y2)) return false
                x += dx
            }
        } else return false
        return true
    }

    override fun canMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return (((((x1 == x2 && Math.abs(y2 - y1) == 1) || (y1 == y2 && Math.abs(x2 - x1) == 1)) ||
                (Math.abs(x2 - x1) == Math.abs(y2 - y1)) &&
                Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 1) &&
                chess.board[x2, y2] == null &&
                !isInvalidMove(x1, y1, x2, y2)) ||
                castlingPossible(x1, y1, x2, y2))
    }

    override fun canKill(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return ((((x1 == x2 && Math.abs(y2 - y1) == 1) || (y1 == y2 && Math.abs(x2 - x1) == 1)) ||
                (Math.abs(x2 - x1) == Math.abs(y2 - y1)) &&
                Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 1) &&
                chess.board[x2, y2] != null &&
                chess.board[x2, y2]!!.team != team &&
                !isInvalidMove(x1, y1, x2, y2))
    }

}