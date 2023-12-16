package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.UUID;

public class EditTrackViewController {

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

    private final DataTrackServices dataTrackServices;
    private final ComMusicServices comMusicServices;
    private final Track track;

    public EditTrackViewController(GlobalController controller, UUID trackId) {
        DataServicesProvider dataServicesProvider = controller.getDataServicesProvider();
        ComServicesProvider comServicesProvider = controller.getComServicesProvider();
        this.dataTrackServices = dataServicesProvider.getDataTrackServices();
        this.comMusicServices = comServicesProvider.getComMusicServices();

        this.track = dataTrackServices.getTrack(trackId);
    }

    @FXML
    public void initialize(){
        txtTitle.setText(track.getTitle());
        txtAuthor.setText(track.getAuthor());
        txtAlbum.setText(track.getAlbum());
        chkShare.setSelected(!track.getPrivateTrack());

        titleError.managedProperty().bind(titleError.visibleProperty());
    }

    @FXML
    public void onClickSave() throws Exception {
        // If the form has error, do not do anything.
        if (!validateForm()) {
            return;
        }

        String author = txtAuthor.getText().isBlank() ? null : txtAuthor.getText();
        String album = txtAlbum.getText().isBlank() ? null : txtAlbum.getText();

        track.setTitle(txtTitle.getText());
        track.setAuthor(author);
        track.setAlbum(album);

        // La musique privée est devenue publique
        if (track.getPrivateTrack() && chkShare.isSelected()) {
            comMusicServices.publishTrack(track.toTrackLite());
        }
        // La musique publique est devenue privée
        else if (!track.getPrivateTrack() && !chkShare.isSelected()) {
            comMusicServices.unpublishTrack(track.toTrackLite());
        }
        // La musique publique est restée publique
        else if (!track.getPrivateTrack() && chkShare.isSelected()) {
            comMusicServices.updateTrack(track.toTrackLite());
        }

        // C'est IMPORTANT de changer cette valeur à la fin !!!
        track.setPrivateTrack(!chkShare.isSelected());

        dataTrackServices.updateTrack(track);

        Stage stage = (Stage) txtTitle.getScene().getWindow();
        stage.close();
    }

    private boolean validateForm() {
        // Title
        boolean hasTitleError = txtTitle.getText().isBlank();
        titleError.setVisible(hasTitleError);

        return !hasTitleError;
    }

}
