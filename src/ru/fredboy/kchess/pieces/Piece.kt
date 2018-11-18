package ru.fredboy.kchess.pieces

import ru.fredboy.kchess.Canvas
import ru.fredboy.utils.Matrix2
import java.awt.Point

abstract class Piece(private val team: Int, private val type: Int) {

    private var moved: Boolean = false

    fun getTeam() = team
    fun getType() = type
    fun isMoved() = moved

    fun madeMove() {
        moved = true
    }

    protected fun isValidMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return team != Canvas.turn % 2 ||
                Canvas.willHelp(board, x1, y1, Point(x2, y2), team)
    }

    abstract fun canMove(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean
    abstract fun canKill(board: Matrix2<Piece?>, x1: Int, y1: Int, x2: Int, y2: Int): Boolean

    fun hasMoves(board: Matrix2<Piece?>, x: Int, y: Int): Boolean {
        for (xx in 0..7) for (yy in 0..7) {
            if (canMove(board, x, y, xx, yy) || canKill(board, x, y, xx, yy)) return true
        }
        return false
    }

    fun getMoves(board: Matrix2<Piece?>, x: Int, y: Int): ArrayList<Point> {
        val moves: ArrayList<Point> = ArrayList()
        for (xx in 0..7) for (yy in 0..7) {
            if (canMove(board, x, y, xx, yy) || canKill(board, x, y, xx, yy)) moves.add(Point(xx, yy))
        }
        return moves
    }

}