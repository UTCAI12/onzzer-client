package fr.utc.onzzer.client.hmi.music;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ListenTrackViewController {
    private final GlobalController controller;

    private final DataTrackServices dataTrackServices;


    public ListenTrackViewController(GlobalController controller, Track track) {
        this.controller = controller;
        this.dataTrackServices = this.controller.getDataServicesProvider().getDataTrackServices();

        this.trackArrayList = this.dataTrackServices.getTracks();
        this.track = track;
        this.trackIndex = trackArrayList.indexOf(track);
    }


    private Track track;

    private int trackIndex;

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
    public void onClickPreviousTrack() {
        if (trackIndex > 0) {
            trackIndex--;
            loadTrackAtIndex(trackIndex);
        } else {
            loadTrackAtIndex(trackArrayList.size() - 1);
        }

        this.onClickPlayPause();
    }

    @FXML
    private void onClickPlayPause() {

        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
        }

        final File file;
        try {
            file = new File(this.track.asMp3File(this.track.getTitle()));
            System.out.println(file);

            if (this.track.getTitle() != null) this.txtTitle.setText(this.track.getTitle());
            if (this.track.getAuthor() != null) this.txtAuthor.setText(this.track.getAuthor());
            if (this.track.getAlbum() != null) this.txtAlbum.setText(this.track.getAlbum());
            this.sound = new Media(file.toURI().toString());
            this.mediaPlayer = new MediaPlayer(sound);

            System.out.println(mediaPlayer);

            this.mediaPlayer.currentTimeProperty().addListener((obs, oldValue, newValue) -> {
                if (this.sound == null)
                    return;
                this.txtCurrentTime.setText((int) newValue.toSeconds() + "s");
                this.txtTrackDuration.setText((int) this.sound.getDuration().toSeconds() + "s" );
                this.sliderTrackDuration.setValue((int) ((newValue.toSeconds() / this.sound.getDuration().toSeconds()) * 100) );
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onClickNextTrack() {
        if (trackIndex < trackArrayList.size() - 1) {
            trackIndex++;
            loadTrackAtIndex(trackIndex);
        } else {
            loadTrackAtIndex(0);
        }

        this.onClickPlayPause();
    }

    private void loadTrackAtIndex(int index) {
        track = trackArrayList.get(index);
        trackIndex = index;
    }
}
