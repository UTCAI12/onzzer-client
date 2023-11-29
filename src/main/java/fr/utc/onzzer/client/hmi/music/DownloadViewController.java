package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.hmi.GlobalController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class DownloadViewController{

    private final GlobalController controller;

    public DownloadViewController(GlobalController controller) {
        this.controller = controller;
    }

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

    }
}
