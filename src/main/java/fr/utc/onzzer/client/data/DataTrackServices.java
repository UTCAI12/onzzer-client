package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.TrackLite;

import java.util.ArrayList;
import java.util.UUID;

public interface DataTrackServices {
    void saveTrack(TrackLite track) throws Exception;
    void updateTrack(TrackLite track) throws Exception;
    TrackLite getTrack(UUID uuid) throws Exception;
    void addTrackToLibrary(UUID uuid);
    void removeAllTracks() throws Exception;
    ArrayList<TrackLite> getTracks();
}