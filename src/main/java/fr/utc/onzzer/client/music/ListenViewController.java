package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ListenViewController {

    private final DataTrackServices dataTrackServices;
    private Track track;
    private int trackIndex;
    private ArrayList<Track> trackArrayList;

    private MediaPlayer mediaPlayer;
    private Media sound;
    private boolean isUserChangingSlider = false;

    public ListenViewController(GlobalController controller) {
        this.dataTrackServices = controller.getDataServicesProvider().getDataTrackServices();
    }

    public ListenViewController(GlobalController controller, UUID trackId) {
        this(controller);
        setTrack(trackId);
    }

    public void setTrack(UUID trackId) {
        this.trackArrayList = this.dataTrackServices.getTracks();
        try {
            this.track = this.dataTrackServices.getTrack(trackId);
        } catch (Exception e) {
            System.err.println("Error while loading track : " + trackId);
        }
        this.trackIndex = trackArrayList.indexOf(track);
    }

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
    @FXML
    private Button closeButton;


    private void initializeTrack() {
        File file = null;
        try {
            file = new File(this.track.asMp3File());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.txtTitle.setText(this.track.getTitle() == null ? "No Title" : this.track.getTitle());
        this.txtAuthor.setText(this.track.getAuthor() == null ? "No Author" : this.track.getAuthor());
        this.txtAlbum.setText(this.track.getAlbum() == null ? "No Album" : this.track.getAlbum());

        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.dispose();
            this.mediaPlayer = null;
        }

        this.sound = new Media(file.toURI().toString());
        this.mediaPlayer = new MediaPlayer(sound);

        if (this.mediaPlayer.getStatus() == MediaPlayer.Status.UNKNOWN) {
            this.mediaPlayer.statusProperty().addListener((obs, oldStatus, newStatus) -> {
                if (newStatus == MediaPlayer.Status.READY) {
                    initializeMedia();
                }
            });
        } else {
            initializeMedia();
        }
    }

    private void initializeMedia() {
        if (this.mediaPlayer.getStatus() != MediaPlayer.Status.READY)
            throw new RuntimeException("The media player is not ready");

        this.sliderTrackDuration.setMax(this.mediaPlayer.getTotalDuration().toMillis());

        this.mediaPlayer.currentTimeProperty().addListener((observable, oldTime, newTime) -> {
            if (!isUserChangingSlider) {
                this.sliderTrackDuration.setValue(newTime.toMillis());
            }

            this.txtCurrentTime.setText((int) newTime.toSeconds() + "s");
            this.txtTrackDuration.setText((int) this.sound.getDuration().toSeconds() + "s" );
        });

        this.mediaPlayer.setOnEndOfMedia(() -> {
            onClickNextTrack();
        });

        this.mediaPlayer.play();
        buttonPlayTrack.setText("Pause");
    }

    @FXML
    public void initialize() {
        sliderTrackDuration.setMin(0);
        sliderTrackDuration.setValue(0);

        this.sliderTrackDuration.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (isUserChangingSlider && this.mediaPlayer != null) {
                this.mediaPlayer.seek(Duration.millis(newValue.doubleValue()));
            }
        });

        this.sliderTrackDuration.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> isUserChangingSlider = true);
        this.sliderTrackDuration.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> isUserChangingSlider = false);

        initializeTrack();
    }

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
        if (this.mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            this.mediaPlayer.play();
            buttonPlayTrack.setText("Pause");
        } else if (this.mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            this.mediaPlayer.pause();
            buttonPlayTrack.setText("Lecture");
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

    private void loadTrackAtIndex(int index) {
        this.mediaPlayer.stop();
        this.mediaPlayer.dispose();
        this.mediaPlayer = null;
        track = trackArrayList.get(index);
        trackIndex = index;

        initializeTrack();
    }

    @FXML
    public void onCloseButton() {
        BorderPane borderPane = (BorderPane) MainClient.getStage().getScene().getRoot();
        this.mediaPlayer.stop();
        this.mediaPlayer.dispose();
        this.mediaPlayer = null;
        borderPane.setBottom(null);
    }

}
