package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.TrackLite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class ListenTrackViewController {
    private final GlobalController controller;
    private final ComMusicServices services;


    public ListenTrackViewController(GlobalController controller) {
        this.controller = controller;
        this.services = this.controller.getComServicesProvider().getComMusicServices();
    }

    private TrackLite track;

    @FXML
    private TextField txtTitle;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtAlbum;

    @FXML
    private TextField txtCurrentTime;

    @FXML
    private TextField txtTrackDuration;

    @FXML
    private Button btnPrevious;

    @FXML
    private Button btnPlayPause;

    @FXML
    private Button btnNext;

    @FXML
    private Slider sliderTrack;

    @FXML
    public void onClickPreviousTrack(ActionEvent actionEvent) {

    }

    @FXML
    public void onClickPlayPause(ActionEvent actionEvent) {

    }

    @FXML
    public void onClickNextTrack(ActionEvent actionEvent) {

    }
}
