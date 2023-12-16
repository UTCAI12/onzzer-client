package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.common.dataclass.communication.SocketMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketManager extends Thread {

    private final ClientCommunicationController clientController;

    private final Socket socket;
    private ObjectOutputStream out;

    public ClientSocketManager(final Socket socket, final ClientCommunicationController clientController) {
        this.clientController = clientController;
        this.socket = socket;

        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(final SocketMessage message) {
        try {
            this.out.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (
                ObjectInputStream in = new ObjectInputStream(this.socket.getInputStream())
        ) {
            while (true) {
                try {
                    SocketMessage receivedMessage = (SocketMessage) in.readObject();
//                    System.out.println("Client: received "+ receivedMessage);
                    this.clientController.onMessage(receivedMessage, this);
                } catch (java.net.SocketException e) {
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        super.start();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}