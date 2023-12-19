package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;
import fr.utc.onzzer.common.services.Listenable;


import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.UUID;

import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;

import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataTrackServicesImpl extends Listenable implements DataTrackServices {
    private DataRepository dataRepository;

    public DataTrackServicesImpl(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        //Verifier que le dossier data/tracks existe, sinon le créer
        String tracksDirectory = "data";
        File directory = new File(tracksDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
        tracksDirectory = "data/tracks";
        directory = new File(tracksDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
        //Lire tous les fichiers .ser du dossier
        /*try {
            File[] files = directory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".ser");
                }
            });

            for (File file : files) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                    Object obj = objectInputStream.readObject();
                    if (obj instanceof Track) {
                        Track track = (Track) obj;
                        this.dataRepository.tracks.add(track);
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Erreur lors de la lecture des fichiers .ser : " + ex.getMessage());
        }*/


    }

    @Override
    public void saveTrack(Track track) throws Exception {
        // On enregistre un fichier .ser contenant le track dans le dossier tracks
        String tracksDirectory = "data/tracks" + File.separator + track.getUserId();
        File directory = new File(tracksDirectory);
        if(!directory.exists()){
            directory.mkdir();
        }
        String filePath = tracksDirectory + File.separator + track.getId() + ".ser";

        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(track); // Écriture de l'objet Track sérialisé dans le fichier
        } catch (Exception ex) {
            throw new Exception("Erreur lors de la création du track en fichier ser : " + ex.getMessage());
        }
        // On enregistre également dans le meme dossier un fichier mp3 contenant le track
        String mp3FilePath = tracksDirectory + File.separator + track.getId() + ".mp3";
        try (FileOutputStream fileOut = new FileOutputStream(mp3FilePath)) {
            fileOut.write(track.getAudio());
        } catch (Exception ex) {
            throw new Exception("Erreur lors de la création du track en fichier mp3: " + ex.getMessage());
        }
        // On enregistre le track dans la liste des tracks
        this.dataRepository.tracks.add(track);
        if(this.dataRepository.user.getId() == track.getUserId()){
            this.dataRepository.user.addTrackToTrackList(track);
            for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
                if (entry.getKey().getId() == track.getUserId()) {
                    //on ajoute le track à la liste des tracks de l'utilisateur
                    entry.getValue().add(track.toTrackLite());
                }
            }
        }
        this.notify(track, Track.class, ModelUpdateTypes.NEW_TRACK);
    }
    @Override
    public void updateTrack(Track track) throws Exception {
        try {
            Track old_track = this.dataRepository.getTrackByID(track.getId());
            this.dataRepository.tracks.remove(old_track);
            this.dataRepository.tracks.add(track);
            this.notify(track, Track.class, ModelUpdateTypes.UPDATE_TRACK);
            //Si le proprietaire de la musique est l'utilisateur connecté, on modifie la musique dans sa liste de musique
            if(this.dataRepository.user.getId() == track.getUserId()){
                List<Track> tracklist = this.dataRepository.user.getTrackList();
                tracklist.remove(old_track);
                tracklist.add(track);
                this.dataRepository.user.setTrackList(tracklist);
                for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
                    if (entry.getKey().getId() == track.getUserId()) {
                        //on ajoute le track à la liste des tracks de l'utilisateur
                        entry.getValue().add(track.toTrackLite());
                    }
                }
            }
        } catch (Exception e) {
            if (this.dataRepository.toDownloadTracks.contains(track.getId())){
                track.setPrivateTrack(true);
                this.saveTrack(track);
                this.dataRepository.toDownloadTracks.remove(track.getId());
                this.notify(track, Track.class, ModelUpdateTypes.TRACK_READY_DOWNLOAD);
            } else {
                this.dataRepository.tracks.add(track);
                this.notify(track, Track.class, ModelUpdateTypes.TRACK_READY_PLAY);
            }
        }
    }

    @Override
    public Track getTrack(UUID uuid) throws Exception {
        return dataRepository.getTrackByID(uuid);
    }
    @Override
    public ArrayList<Track> getTracks(){return this.dataRepository.tracks;}

    @Override
    public ArrayList<TrackLite> getMyTrackLites() {
        ArrayList<TrackLite> tracklites = new ArrayList<TrackLite>();
        for (Track track : this.dataRepository.tracks) {
            tracklites.add(track.toTrackLite());
        }
        return tracklites;
    }

    @Override
    public ArrayList<TrackLite> getOtherTrackLites() {
        ArrayList<TrackLite> tracklites = new ArrayList<TrackLite>();
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if(entry.getValue() != null)
                for (TrackLite trackLite : entry.getValue()) {
                    tracklites.add(trackLite);
                };
        }
        return tracklites;
    }

    @Override
    public ArrayList<TrackLite> getTrackLites() {
        ArrayList<TrackLite> tracklites = new ArrayList<TrackLite>();
        for (Track track : this.dataRepository.tracks) {
            tracklites.add(track.toTrackLite());
        }
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if(entry.getValue() != null)
                for (TrackLite trackLite : entry.getValue()) {
                    tracklites.add(trackLite);
                }
        }
        return tracklites;
    }
    @Override
    public void addTrackToLibrary(UUID uuid) {
        this.dataRepository.toDownloadTracks.add(uuid);
    }
    @Override
    public void removeAllTracks(){
        dataRepository.tracks.clear();
        this.notify(null, Track.class, ModelUpdateTypes.DELETE_ALL_TRACKS);
    }
    @Override
    public void deleteTrack(UUID uuid) throws Exception {
        Track track = this.dataRepository.getTrackByID(uuid);
        this.dataRepository.tracks.remove(track);
        //Retirer les fichiers .ser et .mp3 du dossier tracks
        String tracksDirectory = "data/tracks"+ File.separator + track.getUserId();
        File directory = new File(tracksDirectory);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles(); // Liste des fichiers du répertoire
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        // Manipulation de chaque fichier
                        if (file.getName().equals(track.getId() + ".ser")) {
                            file.delete();
                        }
                        if (file.getName().equals(track.getId() + ".mp3")) {
                            file.delete();
                        }
                    }
                }
            }
        }
        this.notify(track, Track.class, ModelUpdateTypes.DELETE_TRACK);
    }

    @Override
    public void publishTrack(Track track) {
        track.setPrivateTrack(false);
        try {
            this.updateTrack(track);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unpublishTrack(Track track) {
        track.setPrivateTrack(true);
        try {
            this.updateTrack(track);
            //notify

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void publishedTrack(TrackLite trackLite){
        boolean found = false;
        //Modifier dans la hashmap des userLite / tracklites
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if (entry.getKey().getId() == trackLite.getUserId()) {
                if(entry.getValue() == null){
                    entry.setValue(new ArrayList<TrackLite>());
                }
                //on ajoute le track à la liste des tracks de l'utilisateur
                entry.getValue().add(trackLite);
                found = true;
                this.notify(trackLite, TrackLite.class, ModelUpdateTypes.NEW_TRACK);
            }
        }
        if(!found){
            System.err.println("User not found");
        }
    }


    @Override
    public void unpublishedTrack(TrackLite trackLite){
        boolean found = false;
        //Modifier dans la hashmap des userLite / tracklites
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if (entry.getKey().getId() == trackLite.getUserId()) {
                if(entry.getValue() != null)
                    //on remove le track à la liste des tracks de l'utilisateur
                    for (TrackLite trackLite1 : entry.getValue()) {
                        if (trackLite1.getId() == trackLite.getId()) {
                            entry.getValue().remove(trackLite1);
                            found = true;
                            this.notify(trackLite, TrackLite.class, ModelUpdateTypes.DELETE_TRACK);
                            break;
                        }
                    }
                }
        }
        if(!found){
            System.err.println("Track not found");
        }
    }

    @Override
    public void updateTrack(TrackLite trackLite) {
        //Modifier dans la hashmap des userLite / tracklites
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if (entry.getKey().getId() == trackLite.getUserId()) {
                //on ajoute le track à la liste des tracks de l'utilisateur
                for (TrackLite track : entry.getValue()) {
                    if (track.getId() == trackLite.getId()) {
                        entry.getValue().remove(track);
                        entry.getValue().add(trackLite);
                        this.notify(trackLite, TrackLite.class, ModelUpdateTypes.UPDATE_TRACK);
                        break;
                    }
                }
            }
        }

    }

}
