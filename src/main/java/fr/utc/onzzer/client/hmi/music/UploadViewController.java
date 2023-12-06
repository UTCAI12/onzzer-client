package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.util.ValidationResult;
import fr.utc.onzzer.client.hmi.util.ValidationUtil;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    private Path filePath;

    @FXML
    private Label noFileError;

    @FXML
    private Label wrongFileError;

    @FXML
    private VBox inputGroupTitle;

    @FXML
    private VBox inputGroupArtist;

    @FXML
    private VBox inputGroupAlbum;

    @FXML
    private Button btnUpload;

    @FXML
    private Button btnSave;

    public UploadViewController(GlobalController controller) {
        DataServicesProvider dataServicesProvider = controller.getDataServicesProvider();
        this.trackServices = dataServicesProvider.getDataTrackServices();
        this.userServices = dataServicesProvider.getDataUserServices();
    }


    @FXML
    public void onClickUpload(ActionEvent actionEvent) throws IOException {
        noFileError.setVisible(false);
        wrongFileError.setVisible(false);
        Stage stage = MainClient.getStage();
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
        else if (extension.equals("")) {
            noFileError.setVisible(true);
            wrongFileError.setVisible(false);
        }
        else {
            wrongFileError.setVisible(true);
            noFileError.setVisible(false);
        }
    }

    @FXML
    public void onClickSave(ActionEvent actionEvent) throws Exception {
        // If the form has error, do not do anything.
        boolean hasErrors = checkErrors();
        if(hasErrors) return;

        String title = this.onTitleChange().value();
        String author = this.onArtistChange().value();
        String album = this.onAlbumChange().value();
        byte[] file = Files.readAllBytes(this.filePath);

        User user = this.userServices.getUser();
        UUID trackID = UUID.randomUUID();
        fr.utc.onzzer.common.dataclass.Track track = new Track(trackID, user.getId(), title, author);
        track.setAudio(file);
        this.trackServices.saveTrack(track);
        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view"));
        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());
        stage.setScene(scene);
    }
    private boolean checkErrors() throws IOException {
        // Validating inputs.
        ValidationResult<String> title = this.onTitleChange();
        ValidationResult<String> artist = this.onArtistChange();
        ValidationResult<String> album = this.onAlbumChange();

        return title.hasError() || artist.hasError() || album.hasError() || filePath!=null;
    }

    @FXML
    private ValidationResult<String> onTitleChange() {

        boolean hasErrors = false;
        String title = ((TextField) this.inputGroupTitle.lookup(".input")).getText();

        // title must not be empty or blank.
        if(title.isEmpty() || title.isBlank()) {
            ValidationUtil.showError(this.inputGroupTitle);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupTitle);
        }

        return new ValidationResult<>(title, hasErrors);
    }

    private ValidationResult<String> onAlbumChange() {
        String title = ((TextField) this.inputGroupAlbum.lookup(".input")).getText();

        // album is allowed to be empty or blank.
        return new ValidationResult<>(title, false);
    }

    private ValidationResult<String> onArtistChange() {
        String title = ((TextField) this.inputGroupArtist.lookup(".input")).getText();

        // artist is allowed to be empty or blank.
        return new ValidationResult<>(title, false);
    }
}
