package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.impl.ClientCommunicationController;
import fr.utc.onzzer.client.communication.ComMainServices;
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

public class LoginViewController {

    @FXML
    private Button loginButton;

    @FXML
    private Label registerLabel;

    @FXML
    private TextField txtServerIp;

    @FXML
    private TextField txtServerPort;

    @FXML
    private TextField txtUserPseudo;

    @FXML
    private void onRegisterLabelClick() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    @FXML
    private void onLoginButtonClick() throws IOException {
        // TODO temporairement pour voir si ca marche bien. A terme faire une méthode à côté
        User user = new User(UUID.randomUUID(), txtUserPseudo.getText(), "mail", "mdp");
        UserLite userLite = new UserLite(user.getId(), user.getUsername());
        ClientModel clientModel = new ClientModel(user);
        ComMainServices comMainServices = new ClientCommunicationController(
                this.txtServerIp.getText(), Integer.parseInt(this.txtServerPort.getText()), clientModel);
        comMainServices.connect(userLite, new ArrayList<>());
        // TODO Fin

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }
}
