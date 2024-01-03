package fr.utc.onzzer.client.common.services;

import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import java.net.ConnectException;
import java.util.List;

public interface ComMainServices {
    void connect(UserLite user, List<TrackLite> trackList) throws ConnectException;
    void editUser(UserLite user) throws Exception;
    void disconnect() throws Exception;
}