package fr.utc.onzzer.client.music;

//import fr.utc.onzzer.common.dataclass.Track;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.IOException;

public class SearchViewController {
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtAuthor;
    @FXML
    private TextField txtAlbum;
    @FXML
    private TableView<Track> tableTracks;
    @FXML
    private TableColumn<Track, String> columnTitle;
    @FXML
    private TableColumn<Track, String> columnAuthor;
    @FXML
    private TableColumn<Track, String> columnAlbum;
    @FXML
    private TableColumn<Track, Void> columnActions;

    private ObservableList<Track> tracks;

    public SearchViewController(ObservableList<Track> tracks) {
        this.tracks = tracks;
        initTableTracks();
    }

    private void initTableTracks() {
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnActions.setCellFactory(getActionCellFactory());

        tableTracks.setItems(tracks);
    }

    @FXML
    private void onSearchFieldChanged() throws IOException {
        ObservableList<Track> filteredTracks = FXCollections.observableArrayList(tracks);

        // Remove the tracks if it does not correspond to the filters
        filteredTracks.removeIf(track -> !txtTitle.getText().isBlank() && !track.getTitle().toLowerCase().contains(txtTitle.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAuthor.getText().isBlank() && !track.getAuthor().toLowerCase().contains(txtAuthor.getText().toLowerCase()));
        filteredTracks.removeIf(track -> !txtAlbum.getText().isBlank() && !track.getAlbum().toLowerCase().contains(txtAlbum.getText().toLowerCase()));

        tableTracks.setItems(filteredTracks);
    }

    private void onDownloadButtonClick(Track track) {
        // TODO open the download scene
        System.out.println("selectedTrack: " + track);
    }

    private Callback<TableColumn<Track, Void>, TableCell<Track, Void>> getActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Track, Void> call(final TableColumn<Track, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Action");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Track track = getTableView().getItems().get(getIndex());
                            onDownloadButtonClick(track);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };
    }
}
