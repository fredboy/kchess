package ru.fredboy.kchess.pieces

import ru.fredboy.kchess.chess
import java.awt.Point
import java.io.Serializable

abstract class Piece(val team: Int, val type: Int) : Serializable {

    var moved: Boolean = false
        private set(value) {
            field = value
        }

    fun madeMove() {
        moved = true
    }

    fun hasMoves(x: Int, y: Int): Boolean {
        for (xx in 0..7) for (yy in 0..7) {
            if ((x != xx || y != yy) && canMove(x, y, xx, yy) || canKill(x, y, xx, yy)) {
                return true
            }
        }
        return false
    }

    fun getMoves(x: Int, y: Int): ArrayList<Point> {
        val moves: ArrayList<Point> = ArrayList()
        for (xx in 0..7) for (yy in 0..7) {
            if (canMove(x, y, xx, yy) || canKill(x, y, xx, yy)) moves.add(Point(xx, yy))
        }
        return moves
    }

    protected fun isValidMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean {
        return team != chess.turn % 2 || chess.willHelp(x1, y1, Point(x2, y2), team)
    }

    abstract fun canMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean

    abstract fun canKill(x1: Int, y1: Int, x2: Int, y2: Int): Boolean

}