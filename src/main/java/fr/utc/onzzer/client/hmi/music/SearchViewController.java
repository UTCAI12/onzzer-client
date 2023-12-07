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
import javafx.util.Callback;

import java.io.IOException;
import java.util.UUID;

public class SearchViewController {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtAlbum;
    @FXML
    private TableView<TrackLite> tableTracks;
    @FXML
    private TableColumn<TrackLite, String> columnTitle;
    @FXML
    private TableColumn<TrackLite, String> columnAuthor;
    @FXML
    private TableColumn<TrackLite, String> columnAlbum;
    @FXML
    private TableColumn<TrackLite, Void> columnActions;

    private final GlobalController globalController;
    private final DataTrackServices dataTrackServices;
    private ObservableList<TrackLite> tracks;

    public SearchViewController(GlobalController globalController) {
        // Get the controllers and services
        this.globalController = globalController;
        this.dataTrackServices = globalController.getDataServicesProvider().getDataTrackServices();
        DataUserServices dataUserServices = globalController.getDataServicesProvider().getDataUserServices();

        // Adding listeners to know when the users list changes
        dataUserServices.addListener(user -> {
            Platform.runLater(this::refreshTrackList);
        }, UserLite.class, ModelUpdateTypes.NEW_USER);
        dataUserServices.addListener(user -> {
            Platform.runLater(this::refreshTrackList);
        }, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }

    public void initialize() {
        // Automatically resize the columns
        tableTracks.widthProperty().addListener((ov, t, t1) -> {
            final int columnNumber = 3;
            final int columnActionWidth = 100;
            columnTitle.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnAuthor.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnAlbum.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnActions.setPrefWidth(columnActionWidth - 2);
        });

        // Set the cell factories
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnActions.setCellFactory(getActionCellFactory());

        // Get the current accessible tracks
        refreshTrackList();
    }

    private void refreshTrackList() {
        // Get the tracks that are currently accessible on the network
        tracks = FXCollections.observableArrayList(dataTrackServices.getTrackLites());
        // =============== DEBUG =============== //
        UserLite user = new UserLite(UUID.randomUUID(), "Styx");
        UserLite user2 = new UserLite(UUID.randomUUID(), "Batman");
        tracks.add(new TrackLite(UUID.randomUUID(), user.getId(), "Cool title", "Me", "Test"));
        tracks.add(new TrackLite(UUID.randomUUID(), user.getId(), "Test music", "Him", "Test"));
        tracks.add(new TrackLite(UUID.randomUUID(), user.getId(), "Best of", "The author !", "Test 76"));
        tracks.add(new TrackLite(UUID.randomUUID(), user2.getId(), "Silence 10 hours", "That's me", "Test"));
        tracks.add(new TrackLite(UUID.randomUUID(), user2.getId(), "YES", "Me", "Test 367"));
        // =============== DEBUG =============== //
        onSearchFieldChanged();
    }

    @FXML
    private void onSearchFieldChanged() {
        ObservableList<TrackLite> filteredTracks = FXCollections.observableArrayList(tracks);

        // Remove the tracks if it does not correspond to the filters
        filteredTracks.removeIf(track -> !txtTitle.getText().isBlank() && !track.getTitle().toLowerCase().contains(txtTitle.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAuthor.getText().isBlank() && !track.getAuthor().toLowerCase().contains(txtAuthor.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAlbum.getText().isBlank() && !track.getAlbum().toLowerCase().contains(txtAlbum.getText().toLowerCase()));

        // Update the current items displayed
        tableTracks.getItems().removeIf(t -> !filteredTracks.contains(t));
        for (TrackLite track : filteredTracks) {
            if (!tableTracks.getItems().contains(track)) {
                tableTracks.getItems().add(track);
            }
        }
    }

    @FXML
    private void onClearButtonClicked() throws IOException {

        globalController.getViewMusicServices().openCreateTrack();
        // Stop here if there is nothing to change (avoid useless processes)
        if (txtTitle.getText().isBlank()
                && txtAuthor.getText().isBlank()
                && txtAlbum.getText().isBlank()) {
            return;
        }

        txtTitle.setText("");
        txtAuthor.setText("");
        txtAlbum.setText("");
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

    private Callback<TableColumn<TrackLite, Void>, TableCell<TrackLite, Void>> getActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<TrackLite, Void> call(final TableColumn<TrackLite, Void> param) {
                return new TableCell<>() {
                    private final HBox hbox = new HBox();

                    {
                        IconButton btn = new IconButton(IconButton.ICON_DOWNLOAD);
                        btn.setOnAction((ActionEvent event) -> {
                            TrackLite track = getTableView().getItems().get(getIndex());
                            onDownloadButtonClick(track);
                        });

                        hbox.setAlignment(Pos.CENTER);
                        hbox.getChildren().add(btn);
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
}
