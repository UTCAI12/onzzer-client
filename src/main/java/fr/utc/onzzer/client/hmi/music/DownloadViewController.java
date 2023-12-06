package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.UUID;

public class DownloadViewController{

    private final GlobalController controller;
    private final ComMusicServices services;


    public DownloadViewController(GlobalController controller) {
        this.controller = controller;
        this.services = this.controller.getComServicesProvider().getComMusicServices();
    }

    private TrackLite track;

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

    @FXML
    public void onClickDownload(ActionEvent actionEvent) {
        // Removing old errors.
        this.downloadError.setVisible(false);
        this.downloadError.setManaged(false);

        try {
            services.downloadTrack(UUID.randomUUID());
        } catch (Exception exception){
            exception.printStackTrace();
            this.downloadError.setVisible(true);
            this.downloadError.setManaged(true);

            btnDownload.setText("Réessayer");
        }
    }
}
