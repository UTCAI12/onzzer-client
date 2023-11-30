package fr.utc.onzzer.client.hmi.music.services;

import javafx.scene.Scene;

import java.io.IOException;
import java.util.UUID;

public interface ViewMusicServices {
    void openSearchTracks(Scene scene) throws IOException;
    void openCreateTrack() throws IOException;
    void openEditTrack(UUID trackId) throws IOException;
    void openDeleteTrack(UUID trackId) throws IOException;
    void openMediaPlayer(Scene scene, UUID trackId) throws IOException;
}
