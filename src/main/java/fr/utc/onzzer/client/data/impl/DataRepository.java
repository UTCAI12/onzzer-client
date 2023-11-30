package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.dataclass.TrackLite;

import java.util.*;


public class DataRepository {

    public ArrayList<TrackLite> trackLites = new ArrayList<>();

    public HashMap<UserLite, List<TrackLite>> connectedUsers = new HashMap<>();

    public User user;


    TrackLite getTrackByID(UUID trackId){
        int track = 0;
        for (int i =0; i<trackLites.size();i++){
            if(trackLites.get(i).getId()==trackId){
                track = i;
            }
        }
        return this.trackLites.get(track);
    }

    HashMap<UserLite, List<TrackLite>> getConnectedUsers() {
        return this.connectedUsers;
    }
    User getUser() {
        return this.user;
    }

}

