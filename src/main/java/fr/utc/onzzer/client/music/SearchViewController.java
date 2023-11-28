package fr.utc.onzzer.client.music;

import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

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
    private TableView<TrackLite> tableTracks;
    @FXML
    private TableColumn<TrackLite, String> columnTitle;
    @FXML
    private TableColumn<TrackLite, String> columnAuthor;
    @FXML
    private TableColumn<TrackLite, String> columnAlbum;
    @FXML
    private TableColumn<TrackLite, Void> columnActions;

    private ObservableList<TrackLite> tracks = FXCollections.observableArrayList();

    public void setTracks(List<TrackLite> tracks) {
        this.tracks = FXCollections.observableArrayList(tracks);
        tableTracks.setItems(this.tracks);
    }

    public void initialize() {
        UserLite user = new UserLite(UUID.randomUUID(), "Styx");
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Cool title", "Me"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Test music", "Him"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Best of", "The author !"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "Silence 10 hours", "That's me"));
        tracks.add(new TrackLite(UUID.randomUUID(), user, "YES", "Me"));
        initTableTracks();
    }

    private void initTableTracks() {
        // Automatically resize the columns
        tableTracks.widthProperty().addListener((ov, t, t1) -> {
            columnTitle.setPrefWidth((tableTracks.getWidth() - 100) / 3);
            columnAuthor.setPrefWidth((tableTracks.getWidth() - 100) / 3);
            columnAlbum.setPrefWidth((tableTracks.getWidth() - 100) / 3);
            columnActions.setPrefWidth(85);
        });

        // Set the cell factories
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        //columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
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
        //filteredTracks.removeIf(track -> !txtAlbum.getText().isBlank() && !track.getAlbum().toLowerCase().contains(txtAlbum.getText().toLowerCase()));

        tableTracks.setItems(filteredTracks);
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
                    private final Button btn = new Button();
                    private final HBox hbox = new HBox();

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            TrackLite track = getTableView().getItems().get(getIndex());
                            onDownloadButtonClick(track);
                        });
                        btn.getStyleClass().add("icon-button");
                        btn.getStyleClass().add("download-icon");
                        btn.setPickOnBounds(true);

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
