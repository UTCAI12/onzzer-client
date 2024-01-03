package fr.utc.onzzer.client.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.ComMainServices;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MainViewController {

    private final GlobalController controller;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> usersList;

    @FXML
    private Button addMusic;

    @FXML
    private Button search;

    @FXML
    private Button ourMusic;

    @FXML
    private Label username;

    public MainViewController(GlobalController controller) {
        this.controller = controller;

        this.addListeners();

        this.usersList = new ListView<>();
        this.searchField = new TextField();
    }

    public void initialize() {

        // Initializing username.
        this.initializeUsername();

        // Initializing the user list.
        this.initializeUserList();

        // Initializing the track list.
        this.initializeTrackList();
    }

    private void addListeners() {

        DataServicesProvider provider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = provider.getDataUserServices();

        // Adding a listener to get the new user connected and to add it to the list.
        dataUserServices.addListener(user -> {
            Platform.runLater(() -> {
                this.usersList.getItems().add(user.getUsername());
            });
        }, UserLite.class, ModelUpdateTypes.NEW_USER);

        // Adding a listener to get the user disconnected and remove it from the list.
        dataUserServices.addListener(user -> {
            Platform.runLater(() -> {
                this.usersList.getItems().remove(user.getUsername());
            });
        }, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }

    @FXML
    private void handleAddMusic(ActionEvent event) {
        try {
            this.controller.getViewMusicServices().openCreateTrack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUsername() {

        DataServicesProvider provider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = provider.getDataUserServices();

        try {
            User user = dataUserServices.getUser();
            this.username.setText(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            this.username.setText("error");
        }
    }

    private void initializeUserList() {

        // Refresh the user list with the connected users.
        this.refreshUsersList();

        // Removing focus on list view.
        this.usersList.setMouseTransparent(false);
        this.usersList.setFocusTraversable(true);
    }

    private void refreshUsersList() {

        DataServicesProvider provider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = provider.getDataUserServices();

        Map<UserLite, List<TrackLite>> connectedUsers = dataUserServices.getConnectedUsers();
        Collection<UserLite> users = connectedUsers.keySet();

        ObservableList<String> items = this.usersList.getItems();
        items.clear();

        users.forEach(user -> items.add(user.getUsername()));
    }

    private void initializeTrackList() {

        // runLater seems to be necessary, otherwise the view won't load.
        Platform.runLater(this::showMyTrackView);
    }

    @FXML
    private void handleSearch(KeyEvent event) {

        if(event.getCode() != KeyCode.ENTER) {
            return;
        }

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        // Filtering the list using the user input.
        String text = this.searchField.getText().toLowerCase();

        // Refreshing the whole list.
        if (text.isEmpty()) {
            this.refreshUsersList();
            return;
        }

        // Updating the list.
        Map<UserLite, List<TrackLite>> users = dataUserServices.getConnectedUsers();

        ObservableList<String> items = this.usersList.getItems();
        items.clear();

        users.keySet().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(text))
                .forEach(user -> items.add(user.getUsername()));
    }

    @FXML
    private void handleViewOurMusic() {
        this.showMyTrackView();
    }

    @FXML
    private void onMusicAction() {
        try {

            Stage stage = MainClient.getStage();
            Scene scene = stage.getScene();
            this.controller.getViewMusicServices().openSearchTracks(scene);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleSearchMusic() {

        try {

            Stage stage = MainClient.getStage();
            Scene scene = stage.getScene();

            this.controller.getViewMusicServices().openSearchTracks(scene);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleAddMusic() {

        try {
            this.controller.getViewMusicServices().openCreateTrack();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void showMyTrackView() {

        try {
            IHMMainServices services = this.controller.getIHMMainServices();
            services.openTrackList();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void onDisconnect() throws IOException {

        // Even if the disconnection fails, opening the login view to enable
        // the user to reconnect again.
        try {
            ComServicesProvider provider = this.controller.getComServicesProvider();
            ComMainServices services = provider.getComMainServices();
            services.disconnect();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            IHMMainServices services = this.controller.getIHMMainServices();
            services.openLoginView();
        }
    }
}
