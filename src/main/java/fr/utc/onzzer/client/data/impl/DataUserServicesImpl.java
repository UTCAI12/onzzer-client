package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.common.User;

public class DataUserServicesImpl implements DataUserServices {

    public DataUserServicesImpl() {}
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
        return null;
    }
    @Override
    public void addUser(UserLite userLite) throws Exception {}
    @Override
    public void deleteUser(UserLite userLite) throws Exception {}
    @Override
    public void updateUser(User user) throws Exception {}
    @Override
    public void deleteAllUsers() {}
}
