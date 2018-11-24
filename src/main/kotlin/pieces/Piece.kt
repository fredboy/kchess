package ru.fredboy.kchess.pieces

import ru.fredboy.kchess.chess
import ru.fredboy.kchess.willHelp
import java.awt.Point
import java.io.Serializable

abstract class Piece(private val team: Int, private val type: Int) : Serializable {

    private var moved: Boolean = false

    fun getTeam() = team
    fun getType() = type
    fun isMoved() = moved

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
        return team != chess.turn % 2 || willHelp(x1, y1, Point(x2, y2), team)
    }

    abstract fun canMove(x1: Int, y1: Int, x2: Int, y2: Int): Boolean

    abstract fun canKill(x1: Int, y1: Int, x2: Int, y2: Int): Boolean

}