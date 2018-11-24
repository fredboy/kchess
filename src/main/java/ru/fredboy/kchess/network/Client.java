package ru.fredboy.kchess.network;

import ru.fredboy.kchess.Chess;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client extends Networker {

    private Thread clientThread;

    private String address;
    private int port;

    public Client(Chess chess, String address, int port) {
        super(chess);
        this.address = address;
        this.port = port;
        clientThread = new Thread(this);
        clientThread.start();
    }

    @Override
    public Type getType() {
        return Type.CLIENT;
    }

    @Override
    public void run() {

        Thread timeoutThread = new Thread(() -> {
            long timestamp = System.currentTimeMillis();
            while (System.currentTimeMillis() - timestamp < 5000) {
            }
            if (socket == null) {
                JOptionPane.showMessageDialog(chess,
                        "Error: timeout " + (System.currentTimeMillis() - timestamp) + "ms");
                clientThread.interrupt();
            }
        });

        try {
            timeoutThread.start();
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(chess, "Error: " + e.toString());
            timeoutThread.interrupt();
            chess.exitMultiplayer();
        }
        if (socket.isConnected()) {
            JOptionPane.showMessageDialog(chess, "Connected");
            super.run();
        }
    }

}