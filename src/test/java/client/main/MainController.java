package client.main;

import fr.utc.onzzer.client.communication.impl.ClientCommunicationController;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.util.Random;
import java.util.UUID;

public class MainController {
    private final DataServicesProvider dataServicesProvider;
    private final ClientCommunicationController comm;

    @FXML // Adding this annotation (@) means that the Node after will be injected by the FXML Loader.
    private Button buttonAddTrack; // !! The name of the variable (buttonAddTrack) MUST be the same that the name defined in sceneBuilder for this node (fx:id="buttonAddTrack")

    @FXML
    private Label errorPublishTrack;
    @FXML
    private Text username;

    @FXML
    private TextArea onlineUsersList;

    @FXML
    private Button buttonPlayTrack;

    @FXML
    private TextArea trackList;

    @FXML
    private Button buttonPauseTrack;

    @FXML
    private Button buttonSeekTrack;

    @FXML
    private Label labelTrackDuration;

    @FXML
    private Slider sliderTrackDuration;

    @FXML
    private Slider sliderTrackVolume;

    @FXML
    private Label labelTrackVolume;

    @FXML
    private Text labelTrackName;

//    private MediaPlayer mediaPlayer;

//    private Media sound;

    public MainController(final DataServicesProvider dataServicesProvider, final ClientCommunicationController comm) {
        this.dataServicesProvider = dataServicesProvider;
        this.comm = comm;
    }

    @FXML
    private void initialize() {
        try {
            this.initComponents();
            this.initListeners();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void playTrack1() {
//        String musicFile = "src/main/resources/com/ilianazz/mp3/PANJABI_MC_MUNDIAN_TO_BACH_KE_KUBI_REMIX.mp3";
//        this.playMusic(musicFile);
//        this.mediaPlayer.play();
    }

    @FXML
    private void playTrack2() {
//        String musicFile = "src/main/resources/com/ilianazz/mp3/Diorange_Nooran_Sisters_A_R_Rahman_Ali_Ali.mp3";
//        this.playMusic(musicFile);
//        this.mediaPlayer.play();
    }

    @FXML
    private void playTrack3() {
//        String musicFile = "src/main/resources/com/ilianazz/mp3/One_Pound_Fish_Fish_Man.mp3";
//        this.playMusic(musicFile);
//        this.mediaPlayer.play();
    }

    private void playMusic(final String musicFile) {
//        if (this.mediaPlayer != null) {
//            this.mediaPlayer.stop(); // Stop the current media player
//        }
//
//        final File file = new File(musicFile);
//        this.labelTrackName.setText(file.getName());
//        this.sound = new Media(file.toURI().toString());
//        this.mediaPlayer = new MediaPlayer(sound);
//
//        // TODO must use properties here for easier bindings
//        this.mediaPlayer.currentTimeProperty().addListener((obs, oldValue, newValue) -> {
//            if (this.sound == null)
//                return;
//            this.labelTrackDuration.setText((int) newValue.toSeconds() + "s/" + (int) this.sound.getDuration().toSeconds() + "s" );
//            this.sliderTrackDuration.setValue((int) ((newValue.toSeconds() / this.sound.getDuration().toSeconds()) * 100) );
//        });

    }

    private void initComponents() {
        this.buttonPlayTrack.setOnAction(e -> {
//            if (this.mediaPlayer == null)
//                return ;
//
//            this.mediaPlayer.play();
        });
        this.buttonPauseTrack.setOnAction(e -> {
//            if (this.mediaPlayer == null)
//                return ;
//
//            this.mediaPlayer.pause();
        });
        this.buttonSeekTrack.setOnAction(e -> {
//            if (this.mediaPlayer == null)
//                return ;
//
//            this.mediaPlayer.seek(Duration.seconds(this.mediaPlayer.getCurrentTime().toSeconds() + 10));
        });

        // TODO must use properties here for easier bindings
        this.sliderTrackDuration.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (this.sound == null)
//                return;
//
//            if (this.sliderTrackDuration.isValueChanging()) {
//                this.mediaPlayer.seek(Duration.seconds((int)(newValue.doubleValue() / 100 * this.sound.getDuration().toSeconds())));
//            }
        });

        // TODO must use properties here for easier bindings
        this.sliderTrackVolume.valueProperty().addListener((obs, oldValue, newValue) -> {
            this.labelTrackVolume.setText(newValue.intValue() + "%");

//            if (this.sound == null)
//                return;
//
//            this.mediaPlayer.setVolume((Double) newValue / 100.0);
        });

    }

    /**
     * This method must be called AFTER the initialization of the FXML file. Listeners can't be set before because FXML Nodes will be null.
     */
    void initListeners() throws Exception {
        this.buttonAddTrack.setOnAction((e) -> {
            // generating random string
            String trackName = new Random().ints(97, 122 + 1)
                    .limit(5)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();

            // Creating new "track"
            // TODO Use a cast here (if User inherits from UserLite)
            final User u;
            try {
                u = this.dataServicesProvider.getDataUserServices().getUser();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            final UserLite ul = new UserLite(u.getId(), u.getUsername());

            // TODO Use a cast here (if Track inherits from TrackLite)
            final Track track = new Track(UUID.randomUUID(), ul.getId(), trackName, "michel", false);
            final TrackLite newTrackLite = new TrackLite(track.getId(), ul.getId(), trackName, "michel", "ssz");

            // adding track to local model
//            this.dataServicesProvider.getDataTrackServices().addTrack(newTrackLite);

            // Notifying server that a track has been added
            try {
                comm.publishTrack(newTrackLite);
            } catch (Exception ex) {
                this.errorPublishTrack.setText("Error:" + ex);
            }
        });


        // When an event "ModelUpdateTypes.NEW_TRACK" is sent by the server, get the title of this track and display it to "trackList"
        // The function (1st argument) takes in parameter the new track, so we are able to get its name with track.getName()
//        this.dataServicesProvider.getDataTrackServices().addListener(newTrackLite ->
//                        trackList.appendText("-" + newTrackLite.getTitle() + "\n")
//                , TrackLite.class, ModelUpdateTypes.NEW_TRACK);


        // If the model triggers an "NEW_USER" event, call the "updateAllUsers" method
//        this.dataServicesProvider.getDataTrackServices().addListener(newUser -> this.updateUsersView(), UserLite.class, ModelUpdateTypes.NEW_USER);

        // If the model triggers an "DELETE_USER" event, call the "updateAllUsers" method
//        this.dataServicesProvider.getDataTrackServices().addListener(newUser -> this.updateUsersView(), UserLite.class, ModelUpdateTypes.DELETE_USER);

        // initialize the content with all users in the model by calling the "updateAllUsers" method
        this.updateUsersView();

        // set the username
        this.username.setText(this.dataServicesProvider.getDataUserServices().getUser().getUsername());
    }

    private void updateUsersView() {
        // Unlike the "new track", here we clear the displayed content and iterate over all users to recreate the text for each one
        // in order to be able to call this function, no matters if it's delete, create or update user
        this.onlineUsersList.setText("");
//        this.dataServicesProvider.getUsers().forEach(user ->
//                this.onlineUsersList.setText(this.onlineUsersList.getText() + " -" + user.getUsername() + "\n")
//        );
    }


}
