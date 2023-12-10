package fr.utc.onzzer.client.hmi.main;
import java.util.Arrays;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

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
    private TableView<Track> tableau;

    @FXML
    private TableColumn<Track, String> colonneTitre;

    @FXML
    private TableColumn<Track, String> colonneAuteur;

    @FXML
    private Text username;

    private DataUserServices dataUserServices;

    private DataServicesProvider dataServicesProvider;

    public MainViewController(GlobalController controller) {
        this.controller = controller;
        this.colonneTitre = new TableColumn<>();
        this.colonneAuteur = new TableColumn<>();
        this.tableau = new TableView<>();
        this.usersList = new ListView<>();
        this.searchField = new TextField();
    }

    public void initialize() {

        this.dataServicesProvider = this.controller.getDataServicesProvider();
        this.dataUserServices = dataServicesProvider.getDataUserServices();

        // Initializing username.
        this.initializeUsername();

        // Initializing the user list.
        this.initializeUserList();

        // Initializing the music list.
        this.initializeMusicList();
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

        try {
            User user = this.dataUserServices.getUser();
            this.username.setText(user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            this.username.setText("error");
        }
    }

    private void initializeMusicList() {
        colonneTitre.setCellValueFactory(new PropertyValueFactory<>("title"));
        colonneAuteur.setCellValueFactory(new PropertyValueFactory<>("author"));

        try {
            ObservableList<Track> tracks = FXCollections.observableArrayList(dataUserServices.getUser().getTrackList());
            tableau.setItems(tracks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUserList() {

        // Refresh the user list with the connected users.
        this.refreshUsersList();

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

        // Removing focus on list view.
        this.usersList.setMouseTransparent(false);
        this.usersList.setFocusTraversable(true);
    }

    private void refreshUsersList() {

        Map<UserLite, List<TrackLite>> connectedUsers = dataUserServices.getConnectedUsers();
        Collection<UserLite> users = connectedUsers.keySet();

        ObservableList<String> items = this.usersList.getItems();
        items.clear();

        users.forEach(user -> items.add(user.getUsername()));
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
}
