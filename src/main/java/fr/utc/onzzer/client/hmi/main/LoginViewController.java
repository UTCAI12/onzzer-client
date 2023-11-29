package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.ComMainServices;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.util.ValidationResult;
import fr.utc.onzzer.client.hmi.util.ValidationUtil;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class LoginViewController {

    private final GlobalController controller;

    public LoginViewController(GlobalController controller) {
        this.controller = controller;
    }

    @FXML
    private VBox inputGroupIp;

    @FXML
    private VBox inputGroupPort;

    @FXML
    private VBox inputGroupPseudo;

    @FXML
    private VBox inputGroupPassword;

    @FXML
    private Label loginError;

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
    private ValidationResult<Integer> onPortChange() {

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

        return new ValidationResult<>(Integer.parseInt(port), hasErrors);
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

        Node parent = MainClient.getStage().getScene().getRoot();

        // Hiding errors if there are ones.
        ValidationUtil.hideErrors(parent);

        // Validating inputs.
        ValidationResult<String> ip = this.onIpChange();
        ValidationResult<String> login = this.onLoginChange();
        ValidationResult<Integer> port = this.onPortChange();
        ValidationResult<String> password = this.onPasswordChange();

        boolean hasErrors = ip.hasError() || login.hasError() || port.hasError() || password.hasError();

        // If the form has error, do not do anything.
        if(hasErrors) return;

        // Login.
        try {

            // Initializing controller.
            this.controller.initialize(ip.value(), port.value());

            // Providers.
            ComServicesProvider comServicesProvider = this.controller.getComServicesProvider();
            ComMainServices comMainServices = comServicesProvider.getComMainServices();

            DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
            DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

            // Checking credentials.
            boolean hasCredentialsCorrect = dataUserServices.checkCredentials(login.value(), password.value());
            hasCredentialsCorrect = true;

            if(!hasCredentialsCorrect) {
                this.showError("Identifiants incorrects.");
                return;
            }

            // Adding a listener to get the result of the connection.
            dataUserServices.addListener(users -> {
                Platform.runLater(() -> {
                    try {
                        this.openMainView();
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                });
            }, UserLite.class, ModelUpdateTypes.NEW_USERS);

            // Providers.
            User user = new User(UUID.randomUUID(), login.value(), "mail", "mdp");
            UserLite userLite = new UserLite(user.getId(), user.getUsername());

            // Connection to the server.
            comMainServices.connect(userLite, new ArrayList<>());

        } catch (Exception exception) {

            exception.printStackTrace();

            // Showing an error message.
            this.showError("Une erreur est survenue. Veuillez réessayer.");
        }
    }

    private void openMainView() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    private void showError(String text) {
        this.loginError.setText(text);
        this.loginError.setVisible(true);
        this.loginError.setManaged(true);
    }
}
