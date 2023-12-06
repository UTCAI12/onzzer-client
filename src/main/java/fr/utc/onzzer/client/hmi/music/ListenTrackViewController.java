package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class ListenTrackViewController {
    private final GlobalController controller;

    private final DataTrackServices dataTrackServices;


    public ListenTrackViewController(GlobalController controller, Track track) {
        this.controller = controller;
        this.dataTrackServices = this.controller.getDataServicesProvider().getDataTrackServices();

        this.trackArrayList = this.dataTrackServices.getTracks();
        this.track = track;
    }

    private Track track;

    private ArrayList<Track> trackArrayList;

    @FXML
    private Text txtTitle;

    @FXML
    private Text txtAuthor;

    @FXML
    private Text txtAlbum;

    @FXML
    private Text txtCurrentTime;

    @FXML
    private Text txtTrackDuration;

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
        System.out.println(trackArrayList);
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
