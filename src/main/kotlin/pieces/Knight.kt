package ru.fredboy.kchess.pieces

import ru.fredboy.kchess.chess

class Knight(team: Int) : Piece(team, 2) {

    override fun canMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return (((Math.abs(x2 - x1) == 2 && Math.abs(y2-y1) == 1) ||
                (Math.abs(x2 - x1) == 1 && Math.abs(y2-y1) == 2)) &&
                chess.board[x2, y2] == null) && isValidMove(x1, y1, x2, y2)
    }

    override fun canKill(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return (((Math.abs(x2 - x1) == 2 && Math.abs(y2-y1) == 1) ||
                (Math.abs(x2 - x1) == 1 && Math.abs(y2-y1) == 2)) &&
                chess.board[x2, y2] != null &&
                chess.board[x2, y2]!!.team != team) && isValidMove(x1, y1, x2, y2)
    }

}
