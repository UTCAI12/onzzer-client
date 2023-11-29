package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.dataclass.TrackLite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataRepository {

    private ArrayList<TrackLite> trackLites;

    public HashMap<UserLite, List<TrackLite>> connectedUsers = new HashMap<>();

    public User user;

    ArrayList<TrackLite> getTracks(){return this.trackLites;}

    HashMap<UserLite, List<TrackLite>> getConnectedUsers() {
        return this.connectedUsers;
    }
    User getUser() {
        return this.user;
    }

}

/*
public class DataRepository {

    private User user;
    private ArrayList<UserLite> connectedUsers;
    private ArrayList<TrackLite> trackLites;


    public DataRepository() {
        this.connectedUsers = new ArrayList<UserLite>();
    }

    User getUser() {
        return this.me;
    }

    ArrayList<UserLite> getConnectedUsers() {
        return this.connectedUsers;
    }

    ArrayList<TrackLite> getTracks(){return this.trackLites;}
}
*/