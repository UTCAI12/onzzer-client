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
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

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
    }

    @FXML
    private void onClickPlayPause() {
        final File file;
        try {
            System.out.println(this.mediaPlayer);
            file = new File(this.track.asMp3File(this.track.getTitle()));

            if (this.track.getTitle() != null) this.txtTitle.setText(this.track.getTitle());
            if (this.track.getAuthor() != null) this.txtAuthor.setText(this.track.getAuthor());
            if (this.track.getAlbum() != null) this.txtAlbum.setText(this.track.getAlbum());

            this.sound = new Media(file.toURI().toString());

            System.out.println(mediaPlayer);
            if (this.mediaPlayer == null) {
                this.mediaPlayer = new MediaPlayer(sound);
                this.mediaPlayer.setOnEndOfMedia(() -> {
                    onClickNextTrack();
                });
                this.mediaPlayer.play();
            } else {
                if (this.mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    this.mediaPlayer.play();
                } else if (this.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    this.mediaPlayer.pause();
                }
            }
            this.mediaPlayer.currentTimeProperty().addListener((obs, oldValue, newValue) -> {
                if (this.sound == null)
                    return;
                System.out.println(newValue);
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
    }

    @FXML
    private void onSliderReleased(MouseEvent event) {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            double seekTime = (sliderTrackDuration.getValue() / 100) * sound.getDuration().toSeconds();
            mediaPlayer.seek(Duration.seconds(seekTime));
        }
    }

    private void loadTrackAtIndex(int index) {
        this.mediaPlayer.stop();
        this.mediaPlayer = null;
        track = trackArrayList.get(index);
        trackIndex = index;
        this.onClickPlayPause();
    }
}
