package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.TrackLite;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

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
    private Label downloadError;

    private final ComMusicServices services;
    private final TrackLite track;

    public DownloadViewController(GlobalController controller, TrackLite track) {
        this.services = controller.getComServicesProvider().getComMusicServices();
        this.track = track;
    }

    public void initialize() {
        txtTitle.setText(track.getTitle());
        txtAuthor.setText(track.getAuthor());
        //txtAlbum.setText(track.getAlbum());
    }

    @FXML
    public void onClickDownload(ActionEvent actionEvent) {
        // Removing old errors
        this.downloadError.setVisible(false);
        this.downloadError.setManaged(false);

        try {
            this.services.downloadTrack(track.getId());
        } catch (Exception exception){
            exception.printStackTrace();
            this.downloadError.setVisible(true);
            this.downloadError.setManaged(true);

            btnDownload.setText("Réessayer");
        }
    }
}
