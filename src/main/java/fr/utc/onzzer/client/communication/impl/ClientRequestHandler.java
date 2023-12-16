package fr.utc.onzzer.client.communication.impl;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.Comment;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ClientRequestHandler {
    private final DataServicesProvider dataServicesProvider;
    public ClientRequestHandler(final DataServicesProvider dataServicesProvider) {
        this.dataServicesProvider = dataServicesProvider;
    }

    void userConnect(final HashMap<UserLite, List<TrackLite>> connectData) throws Exception {
        UserLite userLite = connectData.keySet().iterator().next();
        List<TrackLite> trackList = connectData.get(userLite);
        for(TrackLite t: trackList){
            this.dataServicesProvider.getDataTrackServices().addTrackToLibrary(t.getId());
        }
        this.dataServicesProvider.getDataUserServices().addUser(userLite);
    }

    void userConnected(final List<UserLite> usersLite) {
        this.dataServicesProvider.getDataUserServices().setConnectedUsers(usersLite);
    }

    void userDisconnect(final UserLite userLite) throws Exception {
        this.dataServicesProvider.getDataUserServices().removeUser(userLite);
    }

    void updateTrack(final TrackLite trackLite) {
        this.dataServicesProvider.getDataTrackServices().updateTrack(trackLite);
    }

    void publishTrack(final TrackLite trackLite) {
        this.dataServicesProvider.getDataTrackServices().publishedTrack(trackLite);

    }

    void unpublishTrack(final TrackLite trackLite) {
        this.dataServicesProvider.getDataTrackServices().unpublishedTrack(trackLite);

    }

    Track getTrack(final UUID trackID) throws Exception {
        return this.dataServicesProvider.getDataTrackServices().getTrack(trackID);
    }

    void publishComment(final ArrayList<Object> comment) throws Exception {
        this.dataServicesProvider.getDataCommentService().addComment((UUID) comment.get(0), (Comment) comment.get(1));
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
