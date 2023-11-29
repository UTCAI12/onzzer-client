package fr.utc.onzzer.client.hmi.music.services;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.UUID;

public interface ViewMusicServices {
    void openSearchTracks(Scene scene) throws IOException;
    void openCreateTrack();
    void openEditTrack(UUID trackId);
    void openDeleteTrack(UUID trackId);
    void openMediaPlayer(Scene scene, UUID trackId);

}
