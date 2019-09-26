package ru.fredboy.kchess.network;

import ru.fredboy.kchess.pieces.Piece;

import java.io.Serializable;

public class Data implements Serializable {

    private Piece[][] board;
    private int turn;


    public Data(Piece[][] board, int turn) {
        this.board = board;
        this.turn = turn;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public int getTurn() {
        return turn;
    }

}
