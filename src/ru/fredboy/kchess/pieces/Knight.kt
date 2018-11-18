package ru.fredboy.kchess.pieces

import ru.fredboy.utils.Matrix2

class Knight(team: Int) : Piece(team, 2) {

    override fun canMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return (((Math.abs(x2 - x1) == 2 && Math.abs(y2-y1) == 1) ||
                (Math.abs(x2 - x1) == 1 && Math.abs(y2-y1) == 2)) &&
                board[x2, y2] == null) && isValidMove(board, x1, y1, x2, y2)
    }

    override fun canKill(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return (((Math.abs(x2 - x1) == 2 && Math.abs(y2-y1) == 1) ||
                (Math.abs(x2 - x1) == 1 && Math.abs(y2-y1) == 2)) &&
                board[x2, y2] != null &&
                board[x2, y2]!!.getTeam() != getTeam()) && isValidMove(board, x1, y1, x2, y2)
    }

}
