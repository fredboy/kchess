package ru.fredboy.network;

import ru.fredboy.kchess.pieces.Piece;
import ru.fredboy.utils.Matrix2;

import java.io.Serializable;

public class Data implements Serializable {

    private Matrix2<Piece> board;
    private int turn;


    public Data(Matrix2<Piece> board, int turn) {
        this.board = board;
        this.turn = turn;
    }

    public Matrix2<Piece> getBoard() {
        return board;
    }

    public int getTurn() {
        return turn;
    }

}
