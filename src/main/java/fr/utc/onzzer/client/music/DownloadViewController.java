package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class DownloadViewController {

    @FXML
    private Text txtTitle;
    @FXML
    private Text txtAuthor;
    @FXML
    private Text txtAlbum;
    @FXML
    private Button btnDownload;
    @FXML
    private VBox errDownload;

    private final ComMusicServices comMusicServices;
    private final ViewMusicServices viewMusicServices;
    private final DataTrackServices dataTrackServices;
    private final TrackLite track;

    public DownloadViewController(GlobalController controller, TrackLite track) {
        this.comMusicServices = controller.getComServicesProvider().getComMusicServices();
        this.viewMusicServices = controller.getViewMusicServices();
        this.track = track;

        this.dataTrackServices = controller.getDataServicesProvider().getDataTrackServices();
        dataTrackServices.addListener(t -> {
            Platform.runLater(() -> onDownloadFinished(t));
        }, Track.class, ModelUpdateTypes.TRACK_READY_DOWNLOAD);
    }

    public void initialize() {
        txtTitle.setText(track.getTitle());
        txtAuthor.setText(track.getAuthor());
        txtAlbum.setText(track.getAlbum());

        btnDownload.setOnAction(this::onClickDownload);

        errDownload.managedProperty().bind(errDownload.visibleProperty());
    }

    private void onDownloadFinished(Track downloadedTrack) {
        if (downloadedTrack.getId().equals(this.track.getId())) {
            btnDownload.setDisable(false);
            btnDownload.setText("Ecouter");
            btnDownload.setOnAction(this::onClickListen);
        }
    }

    public void onClickDownload(ActionEvent actionEvent) {
        // Removing old errors
        errDownload.setVisible(false);

        try {
            this.dataTrackServices.addTrackToLibrary(track.getId());
            this.comMusicServices.downloadTrack(track.getId());
            btnDownload.setDisable(true);
            btnDownload.setText("Téléchargement en cours...");
        } catch (Exception exception) {
            exception.printStackTrace();
            errDownload.setVisible(true);
            btnDownload.setDisable(false);
            btnDownload.setText("Télécharger");
        }
    }

    public void onClickListen(ActionEvent actionEvent) {
        try {
            viewMusicServices.openMediaPlayer(MainClient.getStage().getScene(), track.getId());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void onCloseClick() {
        BorderPane borderPane = (BorderPane) MainClient.getStage().getScene().getRoot();
        borderPane.setBottom(null);
    }
}
