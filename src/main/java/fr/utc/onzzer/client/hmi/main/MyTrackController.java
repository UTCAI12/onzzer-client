package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.UUID;

public class MyTrackController {

    private final GlobalController controller;

    private final DataTrackServices dataTrackServices;
    private final GlobalController globalController;
    @FXML
    private TableView<Track> trackList;

    @FXML
    private TableColumn<Track, String> columnTitle;

    @FXML
    private TableColumn<Track, String> columnAuthor;

    @FXML
    private TableColumn<Track, String> columnAlbum;

    @FXML
    private TableColumn<Track, String> columnShare;

    @FXML
    private TableColumn<Track, Void> columnActions;

    public MyTrackController(GlobalController controller) {
        this.globalController = controller;
        this.dataTrackServices = globalController.getDataServicesProvider().getDataTrackServices();

        this.controller = controller;
    }

    public void initialize() {
        this.initializeTrackList();
    }

    private void initializeTrackList() {

        // Prevent columns from being reordered and resized.
        this.trackList.getColumns().forEach(column -> {
            column.setResizable(false);
            column.setReorderable(false);
        });

        // Specifying values.
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        columnAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        columnShare.setCellValueFactory(cellData -> new SimpleStringProperty( cellData.getValue().getPrivateTrack()?  "Non Partagé" : "Partagé"));
        columnActions.setCellFactory(getActionCellFactory());

        // Make the table responsive.
        this.trackList.widthProperty().addListener((ov, t, t1) -> {

            int columns = this.trackList.getColumns().size();

            // Multiplying by 0.95 remove the horizontal scroll bar on the bottom of the table.
            int width = (int) ((this.trackList.getWidth() / columns) * 0.95);

            columnTitle.setPrefWidth(width);
            columnAuthor.setPrefWidth(width);
            columnAlbum.setPrefWidth(width);
            columnActions.setPrefWidth(width);
            columnShare.setPrefWidth(width);
        });

        // Refresh the whole list.
        this.refreshMusicList();
    }

    private void refreshMusicList() {

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        try {

            ObservableList<Track> items = this.trackList.getItems();
            items.clear();

            // Adding tracks to the list.
            List<Track> tracks = dataUserServices.getUser().getTrackList();
            items.addAll(tracks);

            // TODO : To remove.
            for (int i = 0; i < 100; i++) {
                items.add(new Track(UUID.randomUUID(), UUID.randomUUID(), "title", "author",true));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Callback<TableColumn<Track, Void>, TableCell<Track, Void>> getActionCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Track, Void> call(final TableColumn<Track, Void> param) {
                return new TableCell<>() {
                    private final HBox hbox = new HBox();
                    {
                        // Adding evaluate button.
                        IconButton btnEvaluate = new IconButton(IconButton.ICON_EVALUATE);

                        btnEvaluate.setOnAction((ActionEvent event) -> {
                            Track track = getTableView().getItems().get(getIndex());
                            onEvaluateButtonClick(track);
                        });

                        // Adding remove button.
                        IconButton btnRemove = new IconButton(IconButton.ICON_DELETE);

                        btnRemove.setOnAction((ActionEvent event) -> {
                            Track track = getTableView().getItems().get(getIndex());
                            onRemoveButtonClick(track);
                        });

                        // Adding start listening button.
                        IconButton btnListening = new IconButton(IconButton.ICON_LISTENING);

                        btnListening.setOnAction((ActionEvent event) -> {
                            Track track = getTableView().getItems().get(getIndex());
                            onListeningTrack(track);
                        });

                        this.hbox.setAlignment(Pos.CENTER);
                        this.hbox.getChildren().add(btnListening);
                        this.hbox.getChildren().add(btnEvaluate);
                        this.hbox.getChildren().add(btnRemove);
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

    private void onListeningTrack(Track track) {

        try {
            Stage stage = MainClient.getStage();
            Scene scene = stage.getScene();
            ViewMusicServices viewMusicServices = this.controller.getViewMusicServices();
            viewMusicServices.openMediaPlayer(scene,track.getId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void onRemoveButtonClick(Track track) {
        try {
            // Opening delete track view from the ihm music module.
            ViewMusicServices viewMusicServices = this.controller.getViewMusicServices();
            viewMusicServices.openDeleteTrack(track.getId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void onEvaluateButtonClick(Track track) {
        // Actually, there is no view to evaluate a track.
        // We need to have this information (who's doing that, what code can be called).
    }
}
