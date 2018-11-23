package ru.fredboy.kchess.network;

import ru.fredboy.kchess.Chess;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client extends Networker {

    private String address;
    private int port;

    public Client(Chess chess, String address, int port) {
        super(chess);
        this.address = address;
        this.port = port;
        new Thread(this).start();
    }

    @Override
    public Type getType() {
        return Type.CLIENT;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(chess, "Error: " + e.getMessage());
            chess.exitMultiplayer();
        }
        if (socket.isConnected()) {
            JOptionPane.showMessageDialog(chess, "Connected");
            super.run();
        }
    }

}