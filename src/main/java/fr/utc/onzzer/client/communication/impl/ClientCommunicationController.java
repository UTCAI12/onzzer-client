package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.client.communication.ComMainServices;
import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.common.dataclass.*;
import fr.utc.onzzer.common.dataclass.communication.SocketMessage;
import fr.utc.onzzer.common.dataclass.communication.SocketMessagesTypes;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientCommunicationController implements ComMainServices, ComMusicServices {

    private ClientModel clientModel;

    private final ClientRequestHandler clientRequestHandler;

    private ClientSocketManager clientSocketManager;

    private final String serverAddress;
    private final int serverPort;
    private Socket socket;

    public ClientCommunicationController(final String serverAddress, final int serverPort, final DataServicesProvider dataServicesProvider) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientRequestHandler = new ClientRequestHandler(dataServicesProvider);

        try  {
            this.socket =  new Socket(serverAddress, serverPort);
            this.clientSocketManager = new ClientSocketManager(this.socket, this);
            this.clientSocketManager.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMessage(final SocketMessage message, final ClientSocketManager sender) {
        // TODO replace with a HashMap, check
        switch (message.messageType) {
            case USER_CONNECT -> {
                // If type is USER_CONNECT, we can get the object from the message, we know that it's a UserLite, so must be cast into UserLite
                UserLite userLiteConnected = (UserLite) message.object;

                // call method
                try {
                    this.clientRequestHandler.userConnect(userLiteConnected);
                } catch (Exception e) {
                    System.out.println("Can't connect to server");
                }
            }
            case USER_CONNECTED -> {
                ArrayList<UserLite> users = (ArrayList<UserLite>) message.object;
                this.clientRequestHandler.userConnected(users);
            }
            case USER_DISCONNECT -> {
                UserLite userLiteDisconnected = (UserLite) message.object;
                try {
                    this.clientRequestHandler.userDisconnect(userLiteDisconnected);
                } catch (Exception e) {
                    System.out.println("Can't connect to server");
                }
            }
            case PUBLISH_TRACK -> {
                TrackLite trackLite = (TrackLite) message.object;
                this.clientRequestHandler.publishTrack(trackLite);
            }

            default ->
                    System.out.println("Unhandled message");
        }
    }

    public void sendServer(SocketMessagesTypes messageType, Serializable object) {
        SocketMessage message = new SocketMessage(messageType, object);
        this.clientSocketManager.send(message);
    }

    @Override
    public void connect(UserLite user, List<Track> trackList) throws ConnectException {
        this.sendServer(SocketMessagesTypes.USER_CONNECT, user);
    }

    @Override
    public void editUser(UserLite user) throws Exception {

    }

    @Override
    public void disconnect() throws Exception {

    }

    @Override
    public void downloadTrack(UUID trackId) throws Exception {

    }

    @Override
    public void updateTrack(TrackLite track) throws Exception {

    }

    @Override
    public void publishTrack(TrackLite track) throws Exception {

    }

    @Override
    public void unpublishTrack(TrackLite track) throws Exception {

    }

    @Override
    public void addRating(UUID trackId, Rating rating) throws Exception {

    }

    @Override
    public void addComment(UUID trackId, Comment comment) throws Exception {

    }
}
