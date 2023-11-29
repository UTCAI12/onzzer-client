package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public interface DataUserServices extends Service {
    User importProfile(String filePath) throws Exception;
    void createProfile(User newClient) throws Exception;
    boolean checkCredentials(String user, String pw) throws Exception;
    void exportProfile(User user, String filePath) throws Exception;
    User getUser() throws Exception;
    public List<UserLite> getConnectedUsers();
    void addUser(UserLite userLite) throws Exception;
    void setConnectedUsers(final List<UserLite> connectedUsers);
    void deleteUser(UserLite userLite) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteAllUsers();
}