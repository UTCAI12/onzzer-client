package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.TrackLite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

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
    private Button buttonPlayTrack;

    @FXML
    private Button btnNext;

    @FXML
    private Slider sliderTrackDuration;

    private MediaPlayer mediaPlayer;

    private Media sound;

    @FXML
    public void onClickPreviousTrack(ActionEvent actionEvent) {

    }

    @FXML
    private void onClickPlayPause() {
        /*
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }

        final File file = new File(track);
        this.txtTitle.setText(file.getName());
        this.sound = new Media(file.toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);

        this.mediaPlayer.currentTimeProperty().addListener((obs, oldValue, newValue) -> {
            if (this.sound == null)
                return;
            this.txtCurrentTime.setText((int) newValue.toSeconds() + "s/");
            this.txtTrackDuration.setText((int) this.sound.getDuration().toSeconds() + "s" );
            this.sliderTrackDuration.setValue((int) ((newValue.toSeconds() / this.sound.getDuration().toSeconds()) * 100) );
        });
         */
    }

    @FXML
    public void onClickNextTrack(ActionEvent actionEvent) {

    }
}
