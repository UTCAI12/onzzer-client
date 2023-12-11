package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.Rating;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    void publishRating(final ArrayList<Object> rating) throws Exception {
        this.dataServicesProvider.getDataRatingServices().addRating((UUID) rating.get(0), (Rating) rating.get(1));
    }

    Track getTrack(final UUID trackID) throws Exception {
        return this.dataServicesProvider.getDataTrackServices().getTrack(trackID);
    }

    void serverStopped(){
        // TODO a popup should be added to warn the user that the server stopped. To be discussed with HMI-main
        this.dataServicesProvider.getDataUserServices().logOut();
    }

    void receiveTrack(Track track) throws Exception {
        UUID uuid = track.getId();
        this.dataServicesProvider.getDataTrackServices().addTrackToLibrary(uuid);
        this.dataServicesProvider.getDataTrackServices().updateTrack(track);
    }
}
