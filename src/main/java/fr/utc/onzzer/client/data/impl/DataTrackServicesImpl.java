package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.common.dataclass.TrackLite;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataTrackServicesImpl implements DataTrackServices {
    private Map<UUID, TrackLite> trackMap;

    public DataTrackServicesImpl() {
        this.trackMap = new HashMap<>();
    }
    @Override
    public void saveTrack(TrackLite track) throws Exception {
        trackMap.put(track.getId(), track);
    }
    @Override
    public void updateTrack(TrackLite track) throws Exception {
        if (trackMap.containsKey(track.getId())) {
            trackMap.put(track.getId(), track);
            // Logique de mise à jour de la piste

        } else {
            System.out.println("Error");
        }
    }
    @Override
    public TrackLite getTrack(UUID uuid) throws Exception {
        return trackMap.get(uuid);
    }
    @Override
    public void addTrackToLibrary(UUID uuid) {
        ;
    }
    @Override
    public void removeAllTracks() throws Exception {

    }
}
