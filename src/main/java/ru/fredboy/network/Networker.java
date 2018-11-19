package ru.fredboy.network;

import ru.fredboy.kchess.Chess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Networker implements Runnable {

    public enum Type {
        SERVER,
        CLIENT
    }

    protected Chess chess;

    protected Socket socket;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;

    Networker(Chess chess) {
        this.chess = chess;
    }

    public Data readData() throws IOException{
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

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Data data = readData();
                if (data != null) chess.receiveData(data);
            } catch (IOException e) {
                closeSocket();
                break;
            }
        }
        System.out.println("Socket disconnected.");
        chess.exitMultiplayer();
    }

    public abstract Type getType();

}