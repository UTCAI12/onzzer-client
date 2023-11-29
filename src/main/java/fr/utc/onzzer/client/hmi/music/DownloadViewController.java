package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.TrackLite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DownloadViewController{

    private final GlobalController controller;
    private final ComMusicServices services;


    public DownloadViewController(GlobalController controller) {
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
    private Button btnDownload;

    @FXML
    public void onClickDownload(ActionEvent actionEvent) {
        try {
            services.downloadTrack(track.getId());
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
