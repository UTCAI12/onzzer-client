package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.common.dataclass.Track;

import java.util.UUID;

public class DataTrackServicesImpl implements DataTrackServices {

    public DataTrackServicesImpl() {}
    @Override
    public void saveTrack(Track track) throws Exception {}
    @Override
    public void updateTrack(Track track) throws Exception {}
    @Override
    public Track getTrack(UUID uuid) throws Exception {
        return null;
    }
    @Override
    public void addTrackToLibrary(UUID uuid) {}
    @Override
    public void removeAllTracks() throws Exception {}
}
