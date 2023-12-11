package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.client.hmi.music.SearchViewController;
import fr.utc.onzzer.common.dataclass.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private TableView<TrackLite> musicsList;

    @FXML
    private TableColumn<TrackLite, String> columnTitle;

    @FXML
    private TableColumn<TrackLite, String> columnAuthor;

    @FXML
    private TableColumn<TrackLite, Void> columnActions;

    @FXML
    private TableColumn<TrackLite, String> columnAlbum;

    private DataUserServices dataUserServices;

    private DataServicesProvider dataServicesProvider;

    public MainViewController(GlobalController controller) {
        this.controller = controller;
        this.columnTitle = new TableColumn<>();
        this.columnAuthor = new TableColumn<>();
        this.columnAlbum = new TableColumn<>();
        this.columnActions  = new TableColumn<>();
        this.musicsList = new TableView<>();
        this.usersList = new ListView<>();
        this.searchField = new TextField();
    }

    public void initialize() {

        this.dataServicesProvider = this.controller.getDataServicesProvider();
        this.dataUserServices = dataServicesProvider.getDataUserServices();

        // Initializing the user list.
        this.initializeUserList();
        // Initializing the music list.
        this.initializeMusicList();
    }

    private void initializeMusicList() {

        // Prevent columns from being reordered and resized.
        this.musicsList.getColumns().forEach(column -> {
            column.setResizable(false);
            column.setReorderable(false);
        });

        // Specifying values.
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnActions.setCellFactory(getActionCellFactory());

        // Make the table responsive.
        this.musicsList.widthProperty().addListener((ov, t, t1) -> {

            int columns = this.musicsList.getColumns().size();
            int width = (int) ((this.musicsList.getWidth() / columns) * 0.95);

            columnTitle.setPrefWidth(width);
            columnAuthor.setPrefWidth(width);
            columnAlbum.setPrefWidth(width);
            columnActions.setPrefWidth(width);
        });

        // Refresh the whole list.
        this.refreshMusicList();
    }

    private void refreshMusicList() {

        try {

            ObservableList<TrackLite> items = this.musicsList.getItems();
            items.clear();

            // Adding tracks to the list.
            List<Track> tracks = dataUserServices.getUser().getTrackList();
            tracks.stream().map(Track::toTrackLite).forEach(items::add);

            // TODO : To remove.
            for(int i = 0; i < 100; i++) {
                items.add(new TrackLite(UUID.randomUUID(), UUID.randomUUID(), "title", "author", "author"));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
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

    @FXML
    private void handleViewOurMusic() throws IOException {

        // Get the JavaFx parent element
        Stage stage = MainClient.getStage();
        Scene scene = stage.getScene();

        BorderPane borderPane = (BorderPane) scene.getRoot();

        // Load the view and controller
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
        MainViewController searchViewController = new MainViewController(this.controller);
        fxmlLoader.setController(searchViewController);

        // Update the displayed scene
        borderPane.setCenter(fxmlLoader.load());
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

    private Callback<TableColumn<TrackLite, Void>, TableCell<TrackLite, Void>> getActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<TrackLite, Void> call(final TableColumn<TrackLite, Void> param) {
                return new TableCell<>() {
                    private final HBox hbox = new HBox();

                    {
                        IconButton btnEvaluate = new IconButton(IconButton.ICON_EVALUATE);
                        btnEvaluate.setOnAction((ActionEvent event) -> {
                            TrackLite track = getTableView().getItems().get(getIndex());
                            onEvaluateButtonClick(track);
                        });
                        IconButton btnRemove = new IconButton(IconButton.ICON_DELETE);
                        btnRemove.setOnAction((ActionEvent event) -> {
                            TrackLite track = getTableView().getItems().get(getIndex());
                            onRemoveButtonClick(track);
                        });

                        hbox.setAlignment(Pos.CENTER);
                        hbox.getChildren().add(btnEvaluate);
                        hbox.getChildren().add(btnRemove);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
    }

    private void onRemoveButtonClick(TrackLite track) {
        try
        {
            this.controller.getViewMusicServices().openDeleteTrack(track.getId());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void onEvaluateButtonClick(TrackLite track) {
        try {
            //pas de vue Evaluate track, à voir ce qu'on fait
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
