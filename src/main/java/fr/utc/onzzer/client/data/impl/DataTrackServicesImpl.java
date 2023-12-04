package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Listenable;

import java.util.List;
import java.util.Map;
import java.util.UUID;



import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataTrackServicesImpl extends Listenable implements DataTrackServices {
    private DataRepository dataRepository;

    public DataTrackServicesImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @Override
    public void saveTrack(Track track) throws Exception {
        this.dataRepository.tracks.add(track);
        // Ajouter à l'hashmap des tracks par user
        //this.dataRepository.connectedUsers.put(track.getUserId(), track.toTrackLite());
        //on parcours la hashmap pour trouver l'utilisteur
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if (entry.getKey().getId() == track.getUserId()) {
                //on ajoute le track à la liste des tracks de l'utilisateur
                entry.getValue().add(track.toTrackLite());
            }
        }
    }

    @Override
    public void updateTrack(Track track) throws Exception {
        // Si l'utilisateur un track dans le parametre alors on le met à jour dans la liste des tracks téléchargés, sinon on l'ajoute
        if (this.dataRepository.tracks.contains(track)) {
            this.dataRepository.tracks.set(this.dataRepository.tracks.indexOf(track), track);
        } else {
            this.dataRepository.tracks.add(track);
        }
    }

    @Override
    public Track getTrack(UUID uuid) throws Exception {
        return dataRepository.getTrackByID(uuid);
    }
    @Override
    public ArrayList<Track> getTracks(){return this.dataRepository.tracks;}

    @Override
    public ArrayList<TrackLite> getTrackLites() {
        ArrayList<TrackLite> tracklites = new ArrayList<TrackLite>();
        for (Track track : this.dataRepository.tracks) {
            tracklites.add(track.toTrackLite());
        }
        return tracklites;
    }

    @Override
    public void addTrackToLibrary(UUID uuid) {
        Track track = dataRepository.getTrackByID(uuid);
        //On récupére le fichier audio de la classe track pour l'enregistrer dans un fichier mp3
        byte[] audio = track.getAudio();
        //On enregistre le fichier audio dans un fichier mp3
        String tracksDirectory = "tracks";
        File directory = new File(tracksDirectory);

        // Si le dossier n'existe pas, créez-le
        if (!directory.exists()) {
            directory.mkdir();
        }
        String filePath = tracksDirectory + File.separator + uuid + ".mp3";

        try {
            // Création du FileOutputStream pour écrire les données dans le fichier
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            // Écriture des données audio dans le fichier
            fileOutputStream.write(audio);
            fileOutputStream.close();

            System.out.println("Fichier audio enregistré avec succès : " + filePath);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'enregistrement du fichier audio : " + e.getMessage());
        }
        //on enregistre le track dans la liste des tracks téléchargés
        this.dataRepository.downloadedTracks.add(track);


    }
    @Override
    public void removeAllTracks() throws Exception {
        dataRepository.tracks.clear();
        for(Track track : dataRepository.downloadedTracks){
            String tracksDirectory = "tracks";
            File directory = new File(tracksDirectory);
            String filePath = tracksDirectory + File.separator + track.getId() + ".mp3";
            File file = new File(filePath);
            file.delete();
        }
        dataRepository.downloadedTracks.clear();

    }
}
