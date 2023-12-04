package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;

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
import javafx.util.Callback;

import java.io.IOException;
import java.util.UUID;

public class SearchViewController {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtUser;
    @FXML
    private TableView<TrackLite> tableTracks;
    @FXML
    private TableColumn<TrackLite, String> columnTitle;
    @FXML
    private TableColumn<TrackLite, String> columnAuthor;
    @FXML
    private TableColumn<TrackLite, String> columnUser;
    @FXML
    private TableColumn<TrackLite, Void> columnActions;

    private final GlobalController globalController;
    private final DataTrackServices dataTrackServices;
    private ObservableList<TrackLite> tracks;

    public SearchViewController(GlobalController globalController) {
        this.globalController = globalController;
        this.dataTrackServices = globalController.getDataServicesProvider().getDataTrackServices();
        //this.tracks = FXCollections.observableArrayList(dataTrackServices.getTracks());

        // =============== DEBUG =============== //
        // TODO get all the tracks available on the network and keep it updated
        tracks = FXCollections.observableArrayList();
        UserLite user = new UserLite(UUID.randomUUID(), "Styx");
        UserLite user2 = new UserLite(UUID.randomUUID(), "Batman");
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Cool title", "Me"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Test music", "Him"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Best of", "The author !"));
        tracks.add(new TrackLite(UUID.randomUUID(), user2, "Silence 10 hours", "That's me"));
        tracks.add(new TrackLite(UUID.randomUUID(), user2, "YES", "Me"));
        // =============== DEBUG =============== //
    }

    public void initialize() {
        // Automatically resize the columns
        tableTracks.widthProperty().addListener((ov, t, t1) -> {
            final int columnNumber = 3;
            final int columnActionWidth = 100;
            columnTitle.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnAuthor.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnUser.setPrefWidth((tableTracks.getWidth() - columnActionWidth) / columnNumber);
            columnActions.setPrefWidth(columnActionWidth - 2);
        });

        // Set the cell factories
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnUser.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
        columnActions.setCellFactory(getActionCellFactory());

        // Set the first items
        tableTracks.setItems(tracks);
    }

    @FXML
    private void onSearchFieldChanged() {
        ObservableList<TrackLite> filteredTracks = FXCollections.observableArrayList(tracks);

        // Remove the tracks if it does not correspond to the filters
        filteredTracks.removeIf(track -> !txtTitle.getText().isBlank() && !track.getTitle().toLowerCase().contains(txtTitle.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAuthor.getText().isBlank() && !track.getAuthor().toLowerCase().contains(txtAuthor.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtUser.getText().isBlank() && !track.getUser().getUsername().toLowerCase().contains(txtUser.getText().toLowerCase()));

        tableTracks.setItems(filteredTracks);
    }

    @FXML
    private void onClearButtonClicked() {
        // Stop here if there is nothing to change (avoid useless processes)
        if (txtTitle.getText().isBlank()
                && txtAuthor.getText().isBlank()
                && txtUser.getText().isBlank()) {
            return;
        }

        txtTitle.setText("");
        txtAuthor.setText("");
        txtUser.setText("");
        onSearchFieldChanged();
    }

    private void onDownloadButtonClick(TrackLite track) {
        try {
            // Load the download view
            FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/download-view.fxml"));
            DownloadViewController downloadViewController = new DownloadViewController(globalController);
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
