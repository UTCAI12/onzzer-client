package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Listenable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataUserServicesImpl extends Listenable implements DataUserServices {

    private DataRepository dataRepository;

    public DataUserServicesImpl(final DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
    @Override
    public User importProfile(String filePath) throws Exception {
        return null;
    }
    @Override
    public void createProfile(User newClient) throws Exception {}
    @Override
    public boolean checkCredentials(String user, String pw) throws Exception {
        return false;
    }
    @Override
    public void exportProfile(User user, String filePath) throws Exception {}
    @Override
    public User getUser() throws Exception {
        return this.dataRepository.getUser();
    }

    @Override
    public ArrayList<UserLite> getConnectedUsers() {
        return this.dataRepository.getConnectedUsers();
    }

    @Override
    public void addUser(UserLite userLite) throws Exception {
        this.dataRepository.getConnectedUsers().add(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.NEW_USER);
    }

    @Override
    public void setConnectedUsers(List<UserLite> newConnectedUsers) {
        final ArrayList<UserLite> connectedUsers = this.dataRepository.getConnectedUsers();
        connectedUsers.clear();
        connectedUsers.addAll(newConnectedUsers);

        // TODO change here, to be able to notify without data OR to notify with an array
        final UserLite u = new UserLite(UUID.randomUUID(), "");
        this.notify(u, UserLite.class, ModelUpdateTypes.NEW_USERS);
    }


    @Override
    public void deleteUser(UserLite userLite) throws Exception {
        this.dataRepository.getConnectedUsers().remove(userLite);
        this.notify(userLite, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }
    @Override
    public void updateUser(User user) throws Exception {}
    @Override
    public void deleteAllUsers() {}
}
