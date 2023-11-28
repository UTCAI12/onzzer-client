package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataTrackServices;

import java.util.UUID;

public class DataTrackServicesImpl implements DataTrackServices {

    public DataTrackServicesImpl() {}
    @Override
    void saveTrack(Track track) throws Exception {}
    @Override
    void updateTrack(Track track) throws Exception {}
    @Override
    Track getTrack(UUID uuid) throws Exception {
        return null;
    }
    @Override
    void addTrackToLibrary(UUID uuid) {}
    @Override
    void removeAllTracks() throws Exception {}
}
