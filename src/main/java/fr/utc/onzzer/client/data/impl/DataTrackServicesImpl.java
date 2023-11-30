package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.services.Listenable;

import java.util.ArrayList;
import java.util.UUID;

public class DataTrackServicesImpl extends Listenable implements DataTrackServices {
    private DataRepository dataRepository;

    public DataTrackServicesImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
    @Override
    public void saveTrack(TrackLite track) throws Exception {
        this.dataRepository.trackLites.add(track);
    }
    @Override
    public void updateTrack(TrackLite track) throws Exception {
        if (dataRepository.trackLites.contains(track)) {
            this.dataRepository.trackLites.add(track);
            // Logique de mise à jour de la piste
        } else {
            System.out.println("Error");
        }
    }
    @Override
    public TrackLite getTrack(UUID uuid) throws Exception {
        return dataRepository.getTrackByID(uuid);
    }
    @Override
    public ArrayList<TrackLite> getTracks(){return this.dataRepository.trackLites;}

    @Override
    public void addTrackToLibrary(UUID uuid) {
        TrackLite track = dataRepository.getTrackByID(uuid);
        dataRepository.trackLites.add(track);
    }
    @Override
    public void removeAllTracks() throws Exception {
        dataRepository.trackLites.clear();
    }
}
