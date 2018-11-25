package ru.fredboy.kchess.network;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;

import static ru.fredboy.kchess.MainKt.getChess;

public class Server extends Networker {

    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(this).start();
        JOptionPane.showMessageDialog(getChess(), "Server started.");
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
            JOptionPane.showMessageDialog(getChess(), "Error: " + e.getMessage());
            getChess().exitMultiplayer();
        }
        if (socket.isConnected()) {
            JOptionPane.showMessageDialog(getChess(), "Client connected");
            super.run();
        }
    }

}