package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;

public interface DataUserServices {
    User importProfile(String filePath) throws Exception;
    void createProfile(User newClient) throws Exception;
    boolean checkCredentials(String user, String pw) throws Exception;
    void exportProfile(User user, String filePath) throws Exception;
    User getUser() throws Exception;
    void addUser(UserLite userLite) throws Exception;
    void deleteUser(UserLite userLite) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteAllUsers();
}