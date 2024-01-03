package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Service;

import java.util.HashMap;
import java.util.List;

public interface DataUserServices extends Service {
    User importProfile(String filePath) throws Exception;
    void createProfile(User newClient) throws Exception;
    Boolean checkCredentials(String user, String pw) throws Exception;
    void exportProfile(User user, String filePath) throws Exception;
    User getUser() throws Exception;
    void addUser(UserLite userLite, List<TrackLite> trackLites) throws Exception;
    void setConnectedUsers(final List<UserLite> connectedUsers);
    HashMap<UserLite, List<TrackLite>> getConnectedUsers();

    void deleteUser(UserLite userLite) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteAllUsers();
    void logOut();
    public void removeUser(UserLite userLite) throws Exception;
}