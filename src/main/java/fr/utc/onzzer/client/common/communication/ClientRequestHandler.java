package fr.utc.onzzer.client.common.communication;

import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.util.ArrayList;

public class ClientRequestHandler {
    private final ClientModel model;
    public ClientRequestHandler(final ClientModel model) {
        this.model = model;
    }

    void userConnect(final UserLite userLite) {
        this.model.addUser(userLite);
    }

    void userConnected(final ArrayList<UserLite> usersLite) {
        this.model.addUsers(usersLite);
    }

    void userDisconnect(final UserLite userLite) {
        this.model.removeUser(userLite);
    }

    void publishTrack(final TrackLite trackLite) {
        this.model.addTrack(trackLite);
    }
}
