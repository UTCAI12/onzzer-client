package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class DeleteTrackViewController {
    private final GlobalController controller;
    private final DataTrackServices dataTrackServices;

    private DeleteTrackViewController(GlobalController controller) {
        this.controller = controller;
        this.dataTrackServices = this.controller.getDataServicesProvider().getDataTrackServices();
    }

    public DeleteTrackViewController(GlobalController controller, UUID trackId) {
        this(controller);
        try {
            this.track = this.dataTrackServices.getTrack(trackId);
        } catch (Exception e) {
            System.err.println("Error while loading track : " + trackId);
        }
    }

    public DeleteTrackViewController(GlobalController controller, Track track) {
        this(controller);
        this.track = track;
    }

    private Track track;

    @FXML
    private Text txtTitle;

    @FXML
    private Text txtAuthor;

    @FXML
    private Text txtAlbum;

    @FXML
    private Button btnDelete;



    @FXML
    public void initialize() {
        this.txtTitle.setText(this.track.getTitle() == null ? "No Title" : this.track.getTitle());
        this.txtAuthor.setText(this.track.getAuthor() == null ? "No Author" : this.track.getAuthor());
        this.txtAlbum.setText(this.track.getAlbum() == null ? "No Album" : this.track.getAlbum());
    }

    @FXML
    public void onClickDeleteTrack() {
        try {
            this.dataTrackServices.deleteTrack(track.getId());
        } catch (Exception e) {
            System.err.println("Error while loading track : " + track.getId());
        }

        Stage stage = (Stage) this.btnDelete.getScene().getWindow();
        stage.close();
    }

}
