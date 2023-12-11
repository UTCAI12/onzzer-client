package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.component.IconButton;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import fr.utc.onzzer.common.dataclass.Track;
import fr.utc.onzzer.common.dataclass.TrackLite;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.List;
import java.util.UUID;

public class MyTrackController {

    private final GlobalController controller;

    @FXML
    private TableView<TrackLite> trackList;

    @FXML
    private TableColumn<TrackLite, String> columnTitle;

    @FXML
    private TableColumn<TrackLite, String> columnAuthor;

    @FXML
    private TableColumn<TrackLite, String> columnAlbum;

    @FXML
    private TableColumn<TrackLite, Void> columnActions;

    public MyTrackController(GlobalController controller) {
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
        });

        // Refresh the whole list.
        this.refreshMusicList();
    }

    private void refreshMusicList() {

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        try {

            ObservableList<TrackLite> items = this.trackList.getItems();
            items.clear();

            // Adding tracks to the list.
            List<Track> tracks = dataUserServices.getUser().getTrackList();
            tracks.stream().map(Track::toTrackLite).forEach(items::add);

            // TODO : To remove.
            for (int i = 0; i < 100; i++) {
                items.add(new TrackLite(UUID.randomUUID(), UUID.randomUUID(), "title", "author", "author"));
            }

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
                        // Adding evaluate button.
                        IconButton btnEvaluate = new IconButton(IconButton.ICON_EVALUATE);

                        btnEvaluate.setOnAction((ActionEvent event) -> {
                            TrackLite track = getTableView().getItems().get(getIndex());
                            onEvaluateButtonClick(track);
                        });

                        // Adding remove button.
                        IconButton btnRemove = new IconButton(IconButton.ICON_DELETE);

                        btnRemove.setOnAction((ActionEvent event) -> {
                            TrackLite track = getTableView().getItems().get(getIndex());
                            onRemoveButtonClick(track);
                        });

                        this.hbox.setAlignment(Pos.CENTER);
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

    private void onRemoveButtonClick(TrackLite track) {

        try {
            // Opening delete track view from the ihm music module.
            ViewMusicServices viewMusicServices = this.controller.getViewMusicServices();
            viewMusicServices.openDeleteTrack(track.getId());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void onEvaluateButtonClick(TrackLite track) {
        // Actually, there is no view to evaluate a track.
        // We need to have this information (who's doing that, what code can be called).
    }
}
