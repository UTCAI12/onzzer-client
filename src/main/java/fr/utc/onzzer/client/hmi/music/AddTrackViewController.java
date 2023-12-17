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

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class AddTrackViewController {
    @FXML
    private Text errNoFile;
    @FXML
    private Text errWrongFile;
    @FXML
    private Text txtFileName;
    @FXML
    private TextField txtTitle;
    @FXML
    private Text errTitle;
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
    public void initialize() {
        errNoFile.managedProperty().bind(errNoFile.visibleProperty());
        errWrongFile.managedProperty().bind(errWrongFile.visibleProperty());
        errTitle.managedProperty().bind(errTitle.visibleProperty());
    }

    @FXML
    public void onClickUpload() {
        Stage stage = (Stage) txtTitle.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Fichier Audio");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("AUDIO files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

        if (file == null) {
            return;
        }

        String fileName = file.getName();
        if(fileName.endsWith(".mp3")) {
            errNoFile.setVisible(false);
            errWrongFile.setVisible(false);
            this.filePath = Paths.get(file.getPath());
        } else {
            errWrongFile.setVisible(true);
            errNoFile.setVisible(false);
            this.filePath = null;
        }

        txtFileName.setText(fileName);
    }

    @FXML
    public void onClickSave() throws Exception {
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

        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }

    private boolean validateForm() {
        // Audio file
        if (filePath == null){
            errWrongFile.setVisible(false);
            errNoFile.setVisible(true);
            txtFileName.setText("");
        } else {
            errNoFile.setVisible(false);
        }

        // Title
        boolean hasTitleError = txtTitle.getText().isBlank();
        errTitle.setVisible(hasTitleError);

        return !(hasTitleError || filePath == null);
    }

}
