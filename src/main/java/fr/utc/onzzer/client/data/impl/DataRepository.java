package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.util.ArrayList;

public class DataRepository {

    private User me;

    private ArrayList<UserLite> connectedUsers;

    public DataRepository() {
        this.connectedUsers = new ArrayList<UserLite>();
    }

    User getUser() {
        return this.me;
    }

    ArrayList<UserLite> getConnectedUsers() {
        return this.connectedUsers;
    }

    // TODO all methods to add users/ tracks...
}
