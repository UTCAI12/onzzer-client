package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.util.ValidationResult;
import fr.utc.onzzer.client.hmi.util.ValidationUtil;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import fr.utc.onzzer.client.hmi.GlobalController;


public class UploadViewController {
    private final DataTrackServices trackServices;
    private final DataUserServices userServices;

    private final ComMusicServices musicServices;

    private Path filePath;

    @FXML
    private Label noFileError;

    @FXML
    private Label fileNameLabel;

    @FXML
    private Label wrongFileError;

    @FXML
    private VBox inputGroupTitle;

    @FXML
    private VBox inputGroupArtist;

    @FXML
    private VBox inputGroupAlbum;

    @FXML
    private CheckBox checkboxShare;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnSave;

    @FXML
    private CheckBox chkShare;

    public UploadViewController(GlobalController controller) {
        DataServicesProvider dataServicesProvider = controller.getDataServicesProvider();
        ComServicesProvider comServicesProvider = controller.getComServicesProvider();
        this.trackServices = dataServicesProvider.getDataTrackServices();
        this.userServices = dataServicesProvider.getDataUserServices();
        this.musicServices = comServicesProvider.getComMusicServices();
    }


    @FXML
    public void onClickUpload(ActionEvent actionEvent) throws IOException {
        noFileError.setVisible(false);
        wrongFileError.setVisible(false);
        Stage stage = (Stage) this.btnSave.getScene().getWindow();
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Choisir un fichier");
        File file = fileC.showOpenDialog(stage);
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        String extension = "";
        if (index > 0) {
            extension = fileName.substring(index+1);
        }
        if(extension.equals("mp3")) {
            noFileError.setVisible(false);
            wrongFileError.setVisible(false);
            this.filePath = Paths.get(file.getPath());
        }
        else {
            wrongFileError.setVisible(true);
            noFileError.setVisible(false);
            this.filePath = null;
        }
        fileNameLabel.setText(fileName);
    }

    @FXML
    public void onClickSave(ActionEvent actionEvent) throws Exception {
        // If the form has error, do not do anything.
        boolean hasErrors = checkErrors();
        String title = this.checkTitle().value();
        String author = this.getArtist();
        String album = this.getAlbum();
        boolean isPrivate = !this.checkboxShare.isSelected();

        User user = this.userServices.getUser();
        UUID trackID = UUID.randomUUID();
        if (!hasErrors) {
            fr.utc.onzzer.common.dataclass.Track track = new Track(trackID, filePath.toString(), user.getId(), title, author, isPrivate);
            track.setAlbum(album);
            this.trackServices.saveTrack(track);
            TrackLite tracklite = track.toTrackLite();
            this.musicServices.publishTrack(tracklite);
            Stage stage = (Stage) this.btnSave.getScene().getWindow();
            stage.close();
        }
    }

    private boolean checkErrors() throws IOException {
        // Validating inputs.
        ValidationResult<String> title = this.checkTitle();
        if (title.hasError()){
            ValidationUtil.showError(this.inputGroupTitle);
        }
        else ValidationUtil.hideError(this.inputGroupTitle);
        if (filePath==null){
            wrongFileError.setVisible(false);
            noFileError.setVisible(true);
            fileNameLabel.setText(" ");
        }
        else noFileError.setVisible(false);

        return title.hasError() | filePath==null;
    }

    private ValidationResult<String> checkTitle() {

        boolean hasErrors = false;
        String title = ((TextField) this.inputGroupTitle.lookup(".input")).getText();

        // title must not be empty or blank.
        if(title.isEmpty() | title.isBlank()) {
            hasErrors = true;
        }
        return new ValidationResult<>(title, hasErrors);
    }

    private String getAlbum() {
        // returns the album name
        return ((TextField) this.inputGroupAlbum.lookup(".input")).getText();
    }

    private String getArtist() {
        // returns the album name
        return ((TextField) this.inputGroupArtist.lookup(".input")).getText();
    }

}
