package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.communication.ComMainServices;
import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.util.ValidationResult;
import fr.utc.onzzer.client.hmi.util.ValidationUtil;
import fr.utc.onzzer.common.dataclass.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private TextField pseudoInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Label loginError;

    @FXML
    private Label importError;

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

        String ip = ((TextField) this.inputGroupIp.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(ip.isEmpty() || ip.isBlank()) {
            ValidationUtil.showError(this.inputGroupIp);
            return new ValidationResult<>("", true);
        }

        // Hiding error message.
        ValidationUtil.hideErrors(this.inputGroupIp);

        return new ValidationResult<>(ip, false);
    }

    @FXML
    private ValidationResult<Integer> onPortChange() {

        boolean hasErrors = false;
        String port = ((TextField) this.inputGroupPort.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(port.isEmpty() || port.isBlank() || !ValidationUtil.isInt(port)) {
            ValidationUtil.showError(this.inputGroupPort);
            return new ValidationResult<>(-1, true);
        }

        // Hiding error message.
        ValidationUtil.hideErrors(this.inputGroupPort);

        return new ValidationResult<>(Integer.parseInt(port), hasErrors);
    }

    @FXML
    private ValidationResult<String> onPasswordChange() {

        String password = ((PasswordField) this.inputGroupPassword.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(password.isEmpty() || password.isBlank()) {
            ValidationUtil.showError(this.inputGroupPassword);
            return new ValidationResult<>("", true);
        }

        // Hiding error message.
        ValidationUtil.hideErrors(this.inputGroupPassword);

        return new ValidationResult<>(password, false);
    }

    @FXML
    private void onLoginButtonClick() throws IOException {

        Node parent = MainClient.getStage().getScene().getRoot();

        // Hiding errors if there are ones.
        this.hideGlobalError();

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

            DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
            DataUserServices userServices = dataServicesProvider.getDataUserServices();

            // Checking credentials.
            boolean hasCredentialsCorrect = userServices.checkCredentials(login.value(), password.value());

            if(!hasCredentialsCorrect) {
                this.showGlobalError("Identifiants incorrects.");
                return;
            }

            // Initializing controller.
            this.controller.initialize(ip.value(), port.value());

            ComServicesProvider comServicesProvider = this.controller.getComServicesProvider();
            ComMainServices comServices = comServicesProvider.getComMainServices();

            // Login in the user.
            this.login(userServices, comServices);

        } catch (Exception exception) {

            exception.printStackTrace();

            // Showing an error message.
            this.showGlobalError("Une erreur est survenue. Veuillez réessayer.");
        }
    }

    @FXML
    private void onImportButtonClick() {

        // Hide previous errors.
        this.hideImportError();

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices userServices = dataServicesProvider.getDataUserServices();

        // File selection.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir le fichier de profil.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Profiles", "*.ser"));

        File selectedFile = fileChooser.showOpenDialog(MainClient.getStage());

        if(selectedFile == null) {
            return;
        }

        // Importing profile.
        String filePath = selectedFile.getAbsolutePath();

        try{

            User user = userServices.importProfile(filePath);

            this.pseudoInput.setText(user.getUsername());
            this.passwordInput.setText(user.getPassword());

        } catch (Exception exception){

            exception.printStackTrace();

            this.showImportError();
        }
    }

    private void login(DataUserServices userServices, ComMainServices comServices) throws Exception {

        // Preparing data to send.
        User user = userServices.getUser();

        UserLite userLite = user.toUserLite();

        List<Track> tracks = controller.getDataServicesProvider().getDataTrackServices().getTracks();
        List<TrackLite> publicTrackLites = tracks
                .stream()
                .filter(x -> !x.getPrivateTrack())
                .map(Track::toTrackLite)
                .toList();

        // Adding a listener to get the result of the connection.
        userServices.addListener(this::onLoginSucceeded, UserLite.class, ModelUpdateTypes.NEW_USERS);

        // Connecting to the server.
        comServices.connect(userLite, publicTrackLites);
    }

    private void onLoginSucceeded(UserLite user) {
        Platform.runLater(() -> {
            try {
                this.openMainView();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });
    }

    private void openMainView() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
        MainViewController mainViewController = new MainViewController(this.controller);
        fxmlLoader.setController(mainViewController);

        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    private void showGlobalError(String text) {
        this.loginError.setText(text);
        this.loginError.setVisible(true);
        this.loginError.setManaged(true);
    }

    private void hideGlobalError() {
        this.loginError.setVisible(false);
        this.loginError.setManaged(false);
    }

    private void showImportError() {
        this.importError.setVisible(true);
        this.importError.setManaged(true);
    }

    private void hideImportError() {
        this.importError.setVisible(false);
        this.importError.setManaged(false);
    }
}
