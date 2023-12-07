package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.client.communication.ComMainServices;
import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.*;
import fr.utc.onzzer.common.dataclass.communication.SocketMessage;
import fr.utc.onzzer.common.dataclass.communication.SocketMessagesTypes;

import java.io.IOException;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.util.*;
import java.util.function.BiConsumer;

public class ClientCommunicationController implements ComMainServices, ComMusicServices {

    private ClientModel clientModel;

    private final Map<SocketMessagesTypes, BiConsumer<SocketMessage, ClientSocketManager>> messageHandlers;

    private final ClientRequestHandler clientRequestHandler;

    private ClientSocketManager clientSocketManager;

    private final String serverAddress;
    private final int serverPort;
    private Socket socket;

    public ClientCommunicationController(final String serverAddress, final int serverPort, final DataServicesProvider dataServicesProvider) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.clientRequestHandler = new ClientRequestHandler(dataServicesProvider);

        this.messageHandlers = new HashMap<>();

        messageHandlers.put(SocketMessagesTypes.USER_CONNECT, (message, sender) -> {
            // If type is USER_CONNECT, we can get the object from the message, we know that it's a UserLite, so must be cast into UserLite
            UserLite userLiteConnected = (UserLite) message.object;

            // call method
            try {
                this.clientRequestHandler.userConnect(userLiteConnected);
            } catch (Exception e) {
                System.out.println("Can't connect to server");
            }
        });
        messageHandlers.put(SocketMessagesTypes.USER_CONNECTED, (message, sender) -> {
            ArrayList<UserLite> users = (ArrayList<UserLite>) message.object;
            this.clientRequestHandler.userConnected(users);
        });
        messageHandlers.put(SocketMessagesTypes.USER_DISCONNECT, (message, sender) -> {
            UserLite userLiteDisconnected = (UserLite) message.object;
            try {
                this.clientRequestHandler.userDisconnect(userLiteDisconnected);
            } catch (Exception e) {
                System.out.println("Can't connect to server");
            }
        });
        messageHandlers.put(SocketMessagesTypes.PUBLISH_TRACK, (message, sender) -> {
            TrackLite trackLite = (TrackLite) message.object;
            this.clientRequestHandler.publishTrack(trackLite);
        });
        messageHandlers.put(SocketMessagesTypes.SERVER_PING, (message, sender) -> {
            this.sendServer(SocketMessagesTypes.USER_PING, null);
        });
        messageHandlers.put(SocketMessagesTypes.SERVER_STOPPED, (message, sender) -> {
            System.out.println("I have received a SERVER_STOPPED message from server!");
            this.clientRequestHandler.serverStopped();
        });
        messageHandlers.put(SocketMessagesTypes.GET_TRACK, (message, sender) -> {
            UUID trackId = (UUID) message.object;
            try {
                Track track = this.clientRequestHandler.getTrack(trackId);
                this.sendServer(SocketMessagesTypes.DOWNLOAD_TRACK, track);
            } catch (Exception e) {
                System.out.println("Can't find the track " + trackId);
            }
        });
        messageHandlers.put(SocketMessagesTypes.DOWNLOAD_TRACK, (message, sender) -> {
            Track track = (Track) message.object;
            try {
                this.clientRequestHandler.receiveTrack(track);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        try  {
            this.socket =  new Socket(serverAddress, serverPort);
            this.clientSocketManager = new ClientSocketManager(this.socket, this);
            this.clientSocketManager.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onMessage(final SocketMessage message, final ClientSocketManager sender) {
        // getting the method associated to the message type
        BiConsumer<SocketMessage, ClientSocketManager> handler = messageHandlers.get(message.messageType);

        if (handler != null) {
            // if handler is not null, means that a method is defined
            handler.accept(message, sender);
        } else {
            // if handler is null, no function for this message type
            System.out.println("Unhandled message");
        }
    }

    public void sendServer(SocketMessagesTypes messageType, Serializable object) {
        SocketMessage message = new SocketMessage(messageType, object);
        this.clientSocketManager.send(message);
    }

    @Override
    public void connect(UserLite user, List<TrackLite> trackList) throws ConnectException {
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
        try {
            // Create a new SocketMessage with the type GET_TRACK and the track's UUID as the object.
            this.sendServer(SocketMessagesTypes.GET_TRACK, trackId);
        } catch (Exception e) {
            // Handle any exceptions that may occur during the process.
            throw new Exception("Error sending download track request: " + e.getMessage(), e);
        }
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
