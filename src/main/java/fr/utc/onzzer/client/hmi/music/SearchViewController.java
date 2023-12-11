package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class SearchViewController {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtAlbum;
    @FXML
    private TextField txtUser;
    @FXML
    private TableView<TrackLite> tableTracks;
    @FXML
    private TableColumn<TrackLite, String> columnTitle;
    @FXML
    private TableColumn<TrackLite, String> columnAuthor;
    @FXML
    private TableColumn<TrackLite, String> columnAlbum;
    @FXML
    private TableColumn<TrackLite, String> columnUser;
    @FXML
    private TableColumn<TrackLite, UUID> columnActions;

    private final GlobalController globalController;
    private final DataTrackServices dataTrackServices;
    private final DataUserServices dataUserServices;
    private ObservableList<TrackLite> tracks;

    public SearchViewController(GlobalController globalController) {
        // Get the controllers and services
        this.globalController = globalController;
        this.dataTrackServices = globalController.getDataServicesProvider().getDataTrackServices();
        this.dataUserServices = globalController.getDataServicesProvider().getDataUserServices();

        // Adding listeners to know when the users list changes
        this.dataUserServices.addListener(user -> {
            Platform.runLater(this::refreshTrackList);
        }, UserLite.class, ModelUpdateTypes.NEW_USER);
        this.dataUserServices.addListener(user -> {
            Platform.runLater(this::refreshTrackList);
        }, UserLite.class, ModelUpdateTypes.DELETE_USER);

        // =============== DEBUG =============== //
        try {
            UserLite user1 = new UserLite(UUID.randomUUID(), "Joker");
            UserLite user2 = new UserLite(UUID.randomUUID(), "Batman");
            this.dataUserServices.addUser(user1);
            this.dataUserServices.addUser(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // =============== DEBUG =============== //
    }

    public void initialize() {
        // Automatically resize the columns
        tableTracks.widthProperty().addListener((ov, t, t1) -> {
            final int columnNumber = 4;
            final int columnActionWidth = 100;
            columnTitle.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnAuthor.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnAlbum.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnUser.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnActions.setPrefWidth(columnActionWidth - columnNumber);
        });

        // Set the cell factories
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnUser.setCellValueFactory(cellData -> new SimpleStringProperty(getTrackUsername(cellData.getValue())));
        columnActions.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnActions.setCellFactory(cell -> actionsTableCell());

        // Get the current accessible tracks
        refreshTrackList();
    }

    private void refreshTrackList() {
        // Get the tracks that are currently accessible on the network
        tracks = FXCollections.observableArrayList(dataTrackServices.getTrackLites());

        // =============== DEBUG =============== //
        List<UserLite> users = this.dataUserServices.getConnectedUsers().keySet().stream().toList();
        UserLite user1 = users.get(0);
        UserLite user2 = users.get(1);
        this.tracks.add(new TrackLite(UUID.randomUUID(), user1.getId(), "Doomsday", "Jared Benjamin", null));
        this.tracks.add(new TrackLite(UUID.randomUUID(), user2.getId(), "Human", "Apashe", "2023"));
        this.tracks.add(new TrackLite(UUID.randomUUID(), user2.getId(), "Phoenix", "AViVA", "Rise"));
        this.tracks.add(new TrackLite(UUID.randomUUID(), user1.getId(), "Strangers", "Kenya Grace", null));
        this.tracks.add(new TrackLite(UUID.randomUUID(), user1.getId(), "I'm Just Ken", "Ryan Gosling", "Yes"));
        // =============== DEBUG =============== //

        onSearchFieldChanged();
    }

    @FXML
    private void onSearchFieldChanged() {
        ObservableList<TrackLite> filteredTracks = FXCollections.observableArrayList(tracks);

        // Remove the tracks if it does not correspond to the filters
        filteredTracks.removeIf(track -> !txtTitle.getText().isBlank() && !track.getTitle().toLowerCase().contains(txtTitle.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAuthor.getText().isBlank() && !track.getAuthor().toLowerCase().contains(txtAuthor.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAlbum.getText().isBlank() && track.getAlbum() != null && !track.getAlbum().toLowerCase().contains(txtAlbum.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtUser.getText().isBlank() && !getTrackUsername(track).toLowerCase().contains(txtUser.getText().toLowerCase()));

        // Update the current items displayed
        tableTracks.getItems().removeIf(t -> !filteredTracks.contains(t));
        for (TrackLite track : filteredTracks) {
            if (!tableTracks.getItems().contains(track)) {
                tableTracks.getItems().add(track);
            }
        }
    }

    @FXML
    private void onClearButtonClicked() {
        // Stop here if there is nothing to change (avoid useless processes)
        if (txtTitle.getText().isBlank()
                && txtAuthor.getText().isBlank()
                && txtAlbum.getText().isBlank()
                && txtUser.getText().isBlank()) {
            return;
        }

        txtTitle.setText("");
        txtAuthor.setText("");
        txtAlbum.setText("");
        txtUser.setText("");

        onSearchFieldChanged();
    }

    private void onDownloadButtonClick(TrackLite track) {
        try {
            // Load the download view
            FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/download-view.fxml"));
            DownloadViewController downloadViewController = new DownloadViewController(globalController, track);
            fxmlLoader.setController(downloadViewController);

            // Set the download view in the scene
            BorderPane borderPane = (BorderPane) MainClient.getStage().getScene().getRoot();
            borderPane.setBottom(fxmlLoader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String getTrackUsername(TrackLite track) {
        return this.dataUserServices.getConnectedUsers()
                .keySet()
                .stream()
                .filter(user -> user.getId().equals(track.getUserId()))
                .map(UserLite::getUsername)
                .findFirst()
                .orElse("");
    }

    private TableCell<TrackLite, UUID> actionsTableCell() {
        return new TableCell<TrackLite, UUID>() {
            @Override
            public void updateItem(UUID trackId, boolean empty) {
                super.updateItem(trackId, empty);

                IconButton btn = new IconButton(IconButton.ICON_DOWNLOAD);
                btn.setOnAction((ActionEvent event) -> {
                    System.out.println(trackId);
                    TrackLite track = getTableView().getItems().get(getIndex());
                    onDownloadButtonClick(track);
                });

                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER);
                hbox.getChildren().add(btn);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hbox);
                }
            }
        };
    }
}
