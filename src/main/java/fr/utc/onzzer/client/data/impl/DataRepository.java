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


    Track getTrackByID(UUID trackId){
        int track = 0;
        for (int i =0; i<tracks.size();i++){
            if(tracks.get(i).getId()==trackId){
                track = i;
            }
        }
        return this.tracks.get(track);
    }

    HashMap<UserLite, List<TrackLite>> getConnectedUsers() {
        return this.connectedUsers;
    }
    User getUser() {
        return this.user;
    }

}

