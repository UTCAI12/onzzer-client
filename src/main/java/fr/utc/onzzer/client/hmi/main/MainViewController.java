package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.common.dataclass.Track;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class MainViewController {
    @FXML
    private TableView<Track> tableau;

    @FXML
    private TableColumn<Track, String> colonneTitre;

    @FXML
    private TableColumn<Track, String> colonneAuteur;

    // Initialisation
    public void initialize() {
        colonneTitre.setCellValueFactory(new PropertyValueFactory<>("title"));
        colonneAuteur.setCellValueFactory(new PropertyValueFactory<>("author"));

        ObservableList<Track> tracks = FXCollections.observableArrayList();
        tracks.add(new Track(UUID.randomUUID(), UUID.randomUUID(), "Titre 1", "Auteur 1"));
        tracks.add(new Track(UUID.randomUUID(), UUID.randomUUID(), "Titre 2", "Auteur 2"));

        tableau.setItems(tracks);
    }

    // Autres méthodes et logique du contrôleur
}
