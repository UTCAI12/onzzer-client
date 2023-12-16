package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataTrackServices;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.Track;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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

public class MyTrackController {

    private final GlobalController controller;
    private final DataTrackServices dataTrackServices;
    private final DataUserServices dataUserServices;

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

        this.controller = controller;

        DataServicesProvider provider = controller.getDataServicesProvider();

        this.dataTrackServices = provider.getDataTrackServices();
        this.dataUserServices = provider.getDataUserServices();
    }

    public void initialize() {

        // Initialize the view.
        this.initializeTrackList();

        // Adding listeners.
        this.addListeners();

        // Refresh the whole list.
        this.refreshTrackList();
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
    }

    private void addListeners() {

        // Due to a lack of time, the whole list is refresh even if the change is
        // about only one track. Could be improved.

        this.dataTrackServices.addListener(track -> this.asyncRefreshTrackList(),
                Track.class, ModelUpdateTypes.NEW_TRACK);

        this.dataTrackServices.addListener(track -> this.asyncRefreshTrackList(),
                Track.class, ModelUpdateTypes.NEW_TRACKS);

        this.dataTrackServices.addListener(track -> this.asyncRefreshTrackList(),
                Track.class, ModelUpdateTypes.DELETE_TRACK);

        this.dataTrackServices.addListener(track -> this.asyncRefreshTrackList(),
                Track.class, ModelUpdateTypes.DELETE_ALL_TRACKS);

        this.dataTrackServices.addListener(track -> this.asyncRefreshTrackList(),
                Track.class, ModelUpdateTypes.UPDATE_TRACK);
    }

    private void refreshTrackList() {

        try {

            ObservableList<Track> items = this.trackList.getItems();
            items.clear();

            // Adding tracks to the list.
            List<Track> tracks = this.dataTrackServices.getTracks();
            items.addAll(tracks);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void asyncRefreshTrackList() {
        Platform.runLater(this::refreshTrackList);
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
        try {
            ViewMusicServices viewMusicServices = this.controller.getViewMusicServices();
            viewMusicServices.openEditTrack(track.getId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
