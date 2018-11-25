package ru.fredboy.kchess.network;


import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

import static ru.fredboy.kchess.MainKt.getChess;

public class Client extends Networker {

    private Thread clientThread;

    private String address;
    private int port;

    public Client(String address, int port) {
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
                JOptionPane.showMessageDialog(getChess(),
                        "Error: timeout " + (System.currentTimeMillis() - timestamp) + "ms");
                clientThread.interrupt();
            }
        });

        try {
            timeoutThread.start();
            socket = new Socket(address, port);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(getChess(), "Error: " + e.toString());
            timeoutThread.interrupt();
            getChess().exitMultiplayer();
        }
        if (socket.isConnected()) {
            JOptionPane.showMessageDialog(getChess(), "Connected");
            super.run();
        }
    }

}