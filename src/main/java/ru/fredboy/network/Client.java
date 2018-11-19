package ru.fredboy.network;

import ru.fredboy.kchess.Chess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Networker {

    public Client(Chess chess, String address, int port) throws IOException {
        super(chess);

        this.chess = chess;
        System.out.println("Connecting to " + address + ":" + port);
        socket = new Socket(address, port);
        System.out.println("Connected to " + socket.getRemoteSocketAddress());
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        new Thread(this).start();
    }

    @Override
    public Type getType() {
        return Type.CLIENT;
    }

}