package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.common.communication.ClientCommunicationController;
import fr.utc.onzzer.client.common.services.ComMainServices;
import fr.utc.onzzer.client.common.util.ValidationResult;
import fr.utc.onzzer.client.common.util.ValidationUtil;
import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

public class LoginViewController {

    private final GlobalController controller;

    public LoginViewController(GlobalController controller) {
        this.controller = controller;
    }

    @FXML
    public HBox inputGroupIp;

    @FXML
    public HBox inputGroupPort;

    @FXML
    public VBox inputGroupPseudo;

    @FXML
    public VBox inputGroupPassword;

    @FXML
    private Label registerError;

    @FXML
    private void onRegisterLabelClick() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/register-view.fxml"));

        RegisterViewController registerViewController = new RegisterViewController(this.controller);
        fxmlLoader.setController(registerViewController);

        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    @FXML
    private ValidationResult<String> onLoginChange() {

        boolean hasErrors = false;
        String pseudo = ((TextField) this.inputGroupPseudo.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(pseudo.isEmpty() || pseudo.isBlank()) {
            ValidationUtil.showError(this.inputGroupPseudo);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupPseudo);
        }

        return new ValidationResult<>(pseudo, hasErrors);
    }

    @FXML
    private ValidationResult<String> onIpChange() {

        boolean hasErrors = false;
        String ip = ((TextField) this.inputGroupIp.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(ip.isEmpty() || ip.isBlank()) {
            ValidationUtil.showError(this.inputGroupIp);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupIp);
        }

        return new ValidationResult<>(ip, hasErrors);
    }

    @FXML
    private ValidationResult<String> onPortChange() {

        boolean hasErrors = false;
        String port = ((TextField) this.inputGroupPort.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(port.isEmpty() || port.isBlank()) {
            ValidationUtil.showError(this.inputGroupPort);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupPort);
        }

        return new ValidationResult<>(port, hasErrors);
    }

    @FXML
    private ValidationResult<String> onPasswordChange() {

        boolean hasErrors = false;
        String password = ((PasswordField) this.inputGroupPassword.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(password.isEmpty() || password.isBlank()) {
            ValidationUtil.showError(this.inputGroupPassword);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupPassword);
        }

        return new ValidationResult<>(password, hasErrors);
    }

    @FXML
    private void onLoginButtonClick() throws IOException {
        // TODO temporairement pour voir si ca marche bien. A terme faire une méthode à côté



        //User user = new User(UUID.randomUUID(), txtUserPseudo.getText(), "mail", "mdp");
        boolean hasErrors = checkErrors(MainClient.getStage().getScene().getRoot());

        // If the form has error, do not do anything.
        if(hasErrors) return;


        //UserLite userLite = new UserLite(user.getId(), user.getUsername());


        //ClientModel clientModel = new ClientModel(user);

        try
        {


            /*ComMainServices comMainServices = new ClientCommunicationController(
                    this.txtServerIp.getText(), Integer.parseInt(this.txtServerPort.getText()), clientModel);
            comMainServices.connect(userLite, new ArrayList<>());*/

            Stage stage = MainClient.getStage();
            Scene current = stage.getScene();

            FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

            stage.setScene(scene);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();

            // Showing an error message.
            this.registerError.setVisible(true);
            this.registerError.setManaged(true);
        }

    }

    private boolean checkErrors(Node parent)  {

        // Hiding errors if there are ones.
        ValidationUtil.hideErrors(parent);

        // Validating inputs.
        ValidationResult<String> ip = this.onIpChange();
        ValidationResult<String> login = this.onLoginChange();
        ValidationResult<String> port = this.onPortChange();
        ValidationResult<String> password = this.onPasswordChange();

        boolean hasErrors = ip.hasError() ||
                login.hasError() ||
                port.hasError() ||
                password.hasError();

        return  hasErrors;

    }

}
