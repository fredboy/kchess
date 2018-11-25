package ru.fredboy.kchess.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static ru.fredboy.kchess.MainKt.getChess;

public abstract class Networker implements Runnable {

    public enum Type {
        SERVER,
        CLIENT
    }

    protected Socket socket;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;

    public Data readData() throws IOException {
        try {
            return (Data) input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendData(Data data) {
        try {
            output.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public void closeSocket() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Connected to " + socket.getRemoteSocketAddress());

        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getType() == Type.SERVER) getChess().newGame();

        while (true) {
            try {
                Data data = readData();
                if (data != null) getChess().receiveData(data);
            } catch (IOException e) {
                closeSocket();
                break;
            }
        }
        System.out.println("Socket disconnected.");
        getChess().exitMultiplayer();
    }

    public abstract Type getType();

}
