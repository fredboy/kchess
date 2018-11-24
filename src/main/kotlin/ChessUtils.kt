package ru.fredboy.kchess

import java.awt.Point

fun checkCheck(team: Int): Boolean {
    for (x in 0..7) for (y in 0..7) {
        if (chess.board[x, y] != null && chess.board[x, y]!!.getTeam() != team) {
            for (xx in 0..7) for (yy in 0..7) {
                if (chess.board[x, y]!!.canKill(x, y, xx, yy) && chess.board[xx, yy]!!.getType() == 4) {
                    return true
                }
            }
        }
    }
    return false
}

fun checkMate(team: Int): Boolean {
    if (!chess.check) return false
    for (x in 0..7) for (y in 0..7)
        if (chess.board[x, y] != null && chess.board[x, y]!!.getTeam() == team && chess.board[x, y]!!.hasMoves(x, y))
            return false
    return true
}

fun willHelp(x: Int, y: Int, move: Point, team: Int): Boolean {
    val tmp = chess.board[move.x, move.y]
    chess.board[move.x, move.y] = chess.board[x, y]
    chess.board[x, y] = null
    val tmpBool = checkCheck(team)
    chess.board[x, y] = chess.board[move.x, move.y]
    chess.board[move.x, move.y] = tmp
    return !tmpBool
}