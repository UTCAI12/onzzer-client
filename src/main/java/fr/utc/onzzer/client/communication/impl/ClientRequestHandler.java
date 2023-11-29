package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.util.ArrayList;
import java.util.List;

public class ClientRequestHandler {
    private final DataServicesProvider dataServicesProvider;
    public ClientRequestHandler(final DataServicesProvider dataServicesProvider) {
        this.dataServicesProvider = dataServicesProvider;
    }

    void userConnect(final UserLite userLite) throws Exception {
        this.dataServicesProvider.getDataUserServices().addUser(userLite);
    }

    void userConnected(final List<UserLite> usersLite) {
        this.dataServicesProvider.getDataUserServices().setConnectedUsers(usersLite);
    }

    void userDisconnect(final UserLite userLite) throws Exception {
        this.dataServicesProvider.getDataUserServices().deleteUser(userLite);
    }

    void publishTrack(final TrackLite trackLite) {
//        this.dataServicesProvider.getDataTrackServices().addTrack(trackLite);
    }

    void serverStopped() {
        //TODO disconnect client from server (to be discussed with HMI-Main in order to display a popup)
        System.out.println("I am executing serverStopped method");
    }
}
