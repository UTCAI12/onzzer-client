package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.dataclass.TrackLite;

import java.util.*;


public class DataRepository {


    public ArrayList<Track> tracks = new ArrayList<Track>();

    public ArrayList<Track> downloadedTracks = new ArrayList<Track>();

    public HashMap<UserLite, List<TrackLite>> connectedUsers = new HashMap<>();

    public User user;

    public ArrayList<UUID> toDownloadTracks = new ArrayList<UUID>();

    Track getTrackByID(UUID trackId)  throws Exception  {
        return tracks.stream()
                .filter(t -> t.getId().equals(trackId))
                .findFirst()
                .orElseThrow(() -> new Exception("TrackId " + trackId + " does not exist in the data repository"));
    }

    HashMap<UserLite, List<TrackLite>> getConnectedUsers() {
        return this.connectedUsers;
    }
    User getUser() {
        return this.user;
    }



}

