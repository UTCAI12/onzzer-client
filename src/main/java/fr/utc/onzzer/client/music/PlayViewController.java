package fr.utc.onzzer.client.music;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class PlayViewController {
    @FXML
    public Label titleLabel;
    @FXML
    private Label authorLabel;

    @FXML
    private AnchorPane playContainer;


    @FXML
    public void play() {
        System.out.println("play");
    }
    @FXML
    public void previous() {
        System.out.println("previous");
    }
    @FXML
    public void next() {
        System.out.println("next");
    }
    @FXML
    public void setCurrentMusic(String music) {
        titleLabel.setText(music);
    }

    @FXML
    public void closeView() {
        playContainer.getChildren().clear();
    }
}
