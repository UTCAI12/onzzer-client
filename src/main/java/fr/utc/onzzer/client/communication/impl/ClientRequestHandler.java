package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.util.ArrayList;

public class ClientRequestHandler {
    private final DataServicesProvider dataServicesProvider;
    public ClientRequestHandler(final DataServicesProvider dataServicesProvider) {
        this.dataServicesProvider = dataServicesProvider;
    }

    void userConnect(final UserLite userLite) throws Exception {
        this.dataServicesProvider.getDataUserServices().addUser(userLite);
    }

    void userConnected(final ArrayList<UserLite> usersLite) {
//        this.dataServicesProvider.getDataUserServices().addUsers(usersLite);
    }

    void userDisconnect(final UserLite userLite) throws Exception {
        this.dataServicesProvider.getDataUserServices().deleteUser(userLite);
    }

    void publishTrack(final TrackLite trackLite) {
//        this.dataServicesProvider.getDataTrackServices().addTrack(trackLite);
    }
}
