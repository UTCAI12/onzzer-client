package fr.utc.onzzer.client.common.services;

import fr.utc.onzzer.common.dataclass.Track;

import java.util.UUID;

public interface DataTrackServices {
    void saveTrack(Track track) throws Exception;
    void updateTrack(Track track) throws Exception;
    Track getTrack(UUID uuid) throws Exception;
    void addTrackToLibrary(UUID uuid);
    void removeAllTracks() throws Exception;
}