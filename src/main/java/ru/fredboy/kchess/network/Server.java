package ru.fredboy.kchess.network;

import ru.fredboy.kchess.Chess;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Networker {

    private ServerSocket serverSocket;

    public Server(Chess chess, int port) throws IOException {
        super(chess);
        serverSocket = new ServerSocket(port);
        new Thread(this).start();
        JOptionPane.showMessageDialog(chess, "Server started.");
    }

    @Override
    public Type getType() {
        return Type.SERVER;
    }

    @Override
    public void closeSocket() {
        try {
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(chess, "Error: " + e.getMessage());
            chess.exitMultiplayer();
        }
        if (socket.isConnected()) {
            JOptionPane.showMessageDialog(chess, "Client connected");
            super.run();
        }
    }

}