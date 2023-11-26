package fr.utc.onzzer.client.music;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.common.communication.ClientCommunicationController;
import fr.utc.onzzer.client.common.services.ComMainServices;
import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PlayViewController {
    @FXML
    public Label titleLabel;
    @FXML
    private Label authorLabel;

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
}
