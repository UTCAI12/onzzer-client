package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.communication.ComMusicServices;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.Track;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.UUID;

public class DeleteTrackViewController {

    private final DataTrackServices dataTrackServices;
    private final ComMusicServices comMusicServices;
    private Track track;

    private DeleteTrackViewController(GlobalController controller) {
        this.dataTrackServices = controller.getDataServicesProvider().getDataTrackServices();
        this.comMusicServices = controller.getComServicesProvider().getComMusicServices();
    }

    public DeleteTrackViewController(GlobalController controller, UUID trackId) {
        this(controller);
        this.track = this.dataTrackServices.getTrack(trackId);
    }

    public DeleteTrackViewController(GlobalController controller, Track track) {
        this(controller);
        this.track = track;
    }

    @FXML
    private Text txtTitle;
    @FXML
    private Text txtAuthor;
    @FXML
    private Text txtAlbum;

    @FXML
    public void initialize() {
        this.txtTitle.setText(this.track.getTitle() == null ? "" : this.track.getTitle());
        this.txtAuthor.setText(this.track.getAuthor() == null ? "" : this.track.getAuthor());
        this.txtAlbum.setText(this.track.getAlbum() == null ? "" : this.track.getAlbum());
    }

    @FXML
    public void onClickDeleteTrack() throws Exception {
        // Suppression du fichier
        this.dataTrackServices.deleteTrack(track.getId());

        // Envoi d'un message au serveur si le morceau est publique
        if (!track.getPrivateTrack()) {
            comMusicServices.unpublishTrack(track.toTrackLite());
        }

        Stage stage = (Stage) this.txtTitle.getScene().getWindow();
        stage.close();
    }

}
