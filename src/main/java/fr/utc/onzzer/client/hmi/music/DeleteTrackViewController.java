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
        Track trackItem= this.dataTrackServices.getTrack(trackId);
        this.track = trackItem;
        this.txtTitle.setText(trackItem.getTitle());
        this.txtAuthor.setText(trackItem.getAuthor());
        this.txtAlbum .setText(trackItem.getAlbum());
    }

    public DeleteTrackViewController(GlobalController controller, Track track) {
        this(controller);
        this.track = track;
        this.txtTitle.setText(track.getTitle());
        this.txtAuthor.setText(track.getAuthor());
        this.txtAlbum .setText(track.getAlbum());
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
    public void onClickDeleteTrack() {
        this.dataTrackServices.deleteTrack(track.getId());
        Stage stage = (Stage) this.btnDelete.getScene().getWindow();
        stage.close();
    }

}
