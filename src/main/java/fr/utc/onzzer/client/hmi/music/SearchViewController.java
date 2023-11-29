package fr.utc.onzzer.client.hmi.music;

import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

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

    private final ObservableList<TrackLite> tracks = FXCollections.observableArrayList();

    public void initialize() {
        // TODO get all the tracks available on the network and keep it updated
        UserLite user = new UserLite(UUID.randomUUID(), "Styx");
        UserLite user2 = new UserLite(UUID.randomUUID(), "Batman");
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Cool title", "Me"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Test music", "Him"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Best of", "The author !"));
        tracks.add(new TrackLite(UUID.randomUUID(), user2, "Silence 10 hours", "That's me"));
        tracks.add(new TrackLite(UUID.randomUUID(), user2, "YES", "Me"));

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
        // TODO open the download scene
        System.out.println("selectedTrack: " + track);
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
