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
        //lire tous les fichiers .ser dans le dossier tracks et les ajouter à la liste des tracks
        String tracksDirectory = "tracks";
        File directory = new File(tracksDirectory);
        try(FileInputStream fileInputStream = new FileInputStream(directory);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            Object obj = objectInputStream.readObject();
            if (obj instanceof Track) {
                Track track = (Track) obj;
                this.dataRepository.tracks.add(track);
            }
        } catch (Exception ex) {
            System.out.println("Erreur lors de la lecture des tracks : " + ex.getMessage());
        }
    }

    @Override
    public void saveTrack(Track track) throws Exception {
        // On enregistre un fichier .ser contenant le track dans le dossier tracks
        String tracksDirectory = "tracks";
        File directory = new File(tracksDirectory);
        if(!directory.exists()){
            directory.mkdir();
        }
        String filePath = tracksDirectory + File.separator + track.getId() + ".ser";

        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {
            objectOut.writeObject(track); // Écriture de l'objet Track sérialisé dans le fichier
            System.out.println("Le track a été créé avec succès à l'emplacement : " + filePath);
        } catch (Exception ex) {
            throw new Exception("Erreur lors de la création du track en fichier ser : " + ex.getMessage());
        }
        // On enregistre également dans le meme dossier un fichier mp3 contenant le track
        String mp3FilePath = tracksDirectory + File.separator + track.getId() + ".mp3";
        try (FileOutputStream fileOut = new FileOutputStream(mp3FilePath)) {
            fileOut.write(track.getAudio());
            System.out.println("Le track a été créé avec succès à l'emplacement : " + mp3FilePath);
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
        //old track
        Track old_track = this.dataRepository.getTrackByID(track.getId());
        //Si la musique est déjà présente dans la liste des tracks, on la modifie
        if(this.dataRepository.tracks.contains(old_track)){
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
        }else{
            //Si le trackId est présent dans la liste des track à téléchargé, alors on le télécharge
            if(this.dataRepository.toDownloadTracks.contains(track.getId())){
                this.saveTrack(track);
                this.dataRepository.toDownloadTracks.remove(track.getId());
            }else{
                this.dataRepository.tracks.add(track);
                this.notify(track, Track.class, ModelUpdateTypes.NEW_TRACK);
            }
        }

    }

    @Override
    public Track getTrack(UUID uuid){
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
        this.notify(null, Track.class, ModelUpdateTypes.DELETE_TRACK);
    }
    @Override
    public void deleteTrack(UUID uuid){
        Track track = this.dataRepository.getTrackByID(uuid);
        this.dataRepository.tracks.remove(track);
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
    public void publishedTrack(TrackLite trackLite) {
        //Modifier dans la hashmap des userLite / tracklites
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if (entry.getKey().getId() == trackLite.getUserId()) {
                //on ajoute le track à la liste des tracks de l'utilisateur
                entry.getValue().add(trackLite);
            }
        }
        this.notify(trackLite, TrackLite.class, ModelUpdateTypes.NEW_TRACK);
    }


    @Override
    public void unpublishedTrack(TrackLite trackLite) {
        //Modifier dans la hashmap des userLite / tracklites
        for (Map.Entry<UserLite, List<TrackLite>> entry : this.dataRepository.connectedUsers.entrySet()) {
            if (entry.getKey().getId() == trackLite.getUserId()) {
                //on remove le track à la liste des tracks de l'utilisateur
                for (TrackLite trackLite1 : entry.getValue()) {
                    if (trackLite1.getId() == trackLite.getId()) {
                        entry.getValue().remove(trackLite1);
                        this.notify(trackLite, TrackLite.class, ModelUpdateTypes.DELETE_TRACK);
                        break;
                    }
                }
            }
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
