package ru.fredboy.network;

import ru.fredboy.kchess.Chess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class Server extends Networker{

    public Server(Chess chess, int port) {
        super(chess);
        try {
            this.chess = chess;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client at port " + port + "...");
            socket = serverSocket.accept();
            System.out.println("Connected to " + socket.getRemoteSocketAddress());
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Type getType() {
        return Type.SERVER;
    }

}