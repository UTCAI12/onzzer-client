
package fr.utc.onzzer.client.main;

import fr.utc.onzzer.client.music.PlayViewController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;


public class MainViewController {

    @FXML
    private VBox buttonsContainer;
    @FXML
    private VBox musicViewContainer;

    private boolean enfantAffiche = true;
    private String currentMusic = "None";

    /**
     * This method is automatically called by JavaFX on controller instantiation
     */
    public void initialize() {
        List<String> data = Arrays.asList("Musique 1", "Musique 2", "Musique 3");
        for (String item : data) {
            Button button = new Button(item);
            button.setOnAction(event -> dislayMusic(item));
            buttonsContainer.getChildren().add(button);
        }

        System.out.println("initMain");

    }
    @FXML
    public void dislayMusic(String item) {
        toggleEnfant(item);

    }
    @FXML
    public void toggleEnfant(String item) {
        musicViewContainer.getChildren().clear();
        loadEnfant(item);
        enfantAffiche = true;
    }

    private void loadEnfant(String item) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/music_play-view.fxml"));
            Node enfantNode = loader.load();

            currentMusic=item;
            musicViewContainer.getChildren().add(enfantNode);
            PlayViewController playController = loader.getController();
            playController.setCurrentMusic(item);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
