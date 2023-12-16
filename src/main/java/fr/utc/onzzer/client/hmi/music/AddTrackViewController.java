package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.client.hmi.GlobalController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class AddTrackViewController {
    @FXML
    private Button btnUpload;
    @FXML
    private Text noFileError;
    @FXML
    private Text wrongFileError;
    @FXML
    private Text fileNameLabel;
    @FXML
    private TextField txtTitle;
    @FXML
    private Text titleError;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtAlbum;
    @FXML
    private CheckBox chkShare;

    private final DataTrackServices trackServices;
    private final DataUserServices userServices;
    private final ComMusicServices musicServices;
    private Path filePath;

    public AddTrackViewController(GlobalController controller) {
        DataServicesProvider dataServicesProvider = controller.getDataServicesProvider();
        ComServicesProvider comServicesProvider = controller.getComServicesProvider();
        this.trackServices = dataServicesProvider.getDataTrackServices();
        this.userServices = dataServicesProvider.getDataUserServices();
        this.musicServices = comServicesProvider.getComMusicServices();
    }

    @FXML
    public void onClickUpload(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) this.btnUpload.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Fichier Audio");
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        String fileName = file.getName();
        if(fileName.endsWith(".mp3")) {
            noFileError.setVisible(false);
            wrongFileError.setVisible(false);
            this.filePath = Paths.get(file.getPath());
        } else {
            wrongFileError.setVisible(true);
            noFileError.setVisible(false);
            this.filePath = null;
        }

        fileNameLabel.setText(fileName);
    }

    @FXML
    public void onClickSave(ActionEvent actionEvent) throws Exception {
        // If the form has error, do not do anything.
        if (!validateForm()) {
            return;
        }

        String author = txtAuthor.getText().isBlank() ? null : txtAuthor.getText();
        String album = txtAlbum.getText().isBlank() ? null : txtAlbum.getText();
        User user = this.userServices.getUser();
        UUID trackID = UUID.randomUUID();

        Track track = new Track(trackID, filePath.toString(), user.getId(), txtTitle.getText(), author, !chkShare.isSelected());
        track.setAlbum(album);

        this.trackServices.saveTrack(track);

        if (!track.getPrivateTrack()) {
            TrackLite tracklite = track.toTrackLite();
            this.musicServices.publishTrack(tracklite);
        }

        Stage stage = (Stage) this.btnUpload.getScene().getWindow();
        stage.close();
    }

    private boolean validateForm() throws IOException {
        // Audio file
        if (filePath == null){
            wrongFileError.setVisible(false);
            noFileError.setVisible(true);
            fileNameLabel.setText("");
        } else {
            noFileError.setVisible(false);
        }

        // Title
        boolean hasTitleError = txtTitle.getText().isBlank();
        titleError.setVisible(hasTitleError);

        return !(hasTitleError || filePath == null);
    }

}
