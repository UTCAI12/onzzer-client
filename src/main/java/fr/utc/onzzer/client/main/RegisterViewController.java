package fr.utc.onzzer.client.main;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.common.util.ValidationResult;
import fr.utc.onzzer.client.common.util.ValidationUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class RegisterViewController {

    @FXML
    private Label connectionLabel;

    @FXML
    private Button registrationButton;

    @FXML
    public VBox inputGroupEmail;

    @FXML
    public VBox inputGroupFirstName;

    @FXML
    public VBox inputGroupLastName;

    @FXML
    public VBox inputGroupPseudo;

    @FXML
    public VBox inputGroupBirthDate;

    @FXML
    public VBox inputGroupPassword;

    @FXML
    public VBox inputGroupConfirmPassword;

    @FXML
    private void onConnectionLabelClick() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    @FXML
    private void onRegistrationButtonClick() throws IOException {

        boolean hasErrors = checkErrors(MainClient.getStage().getScene().getRoot());

        // If the form has error, do not do anything.
        if(hasErrors) return;

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    private boolean checkErrors(Node parent) {

        // Hiding errors if there are ones.
        ValidationUtil.hideErrors(parent);

        // Validating inputs.
        ValidationResult<String> email = this.onEmailChange();
        ValidationResult<String> firstName = this.onFirstNameChange();
        ValidationResult<String> lastName = this.onLastNameChange();
        ValidationResult<String> pseudo = this.onPseudoChange();
        ValidationResult<LocalDate> birthDate = this.onBirthdateChange();
        ValidationResult<String> password = this.onPasswordChange();

        return email.hasError() || firstName.hasError() || lastName.hasError() || pseudo.hasError() || birthDate.hasError() || password.hasError();
    }

    @FXML
    private ValidationResult<String> onEmailChange() {

        boolean hasErrors = false;
        String email = ((TextField) this.inputGroupEmail.lookup(".input")).getText();

        // Email must not be empty or blank.
        if(email.isEmpty() || email.isBlank()) {
            ValidationUtil.showError(this.inputGroupEmail);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupEmail);
        }

        return new ValidationResult<>(email, hasErrors);
    }

    @FXML
    private ValidationResult<String> onFirstNameChange() {

        boolean hasErrors = false;
        String firstName = ((TextField) this.inputGroupFirstName.lookup(".input")).getText();

        // Firstname must not be empty or blank.
        if(firstName.isEmpty() || firstName.isBlank()) {
            ValidationUtil.showError(this.inputGroupFirstName);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupFirstName);
        }

        return new ValidationResult<>(firstName, hasErrors);
    }

    @FXML
    private ValidationResult<String> onLastNameChange() {

        boolean hasErrors = false;
        String lastName = ((TextField) this.inputGroupLastName.lookup(".input")).getText();

        // Lastname must not be empty or blank.
        if(lastName.isEmpty() || lastName.isBlank()) {
            ValidationUtil.showError(this.inputGroupLastName);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupLastName);
        }

        return new ValidationResult<>(lastName, hasErrors);
    }

    @FXML
    private ValidationResult<String> onPseudoChange() {

        boolean hasErrors = false;
        String pseudo = ((TextField) this.inputGroupPseudo.lookup(".input")).getText();

        // Pseudo must not be empty or blank.
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
    private ValidationResult<LocalDate> onBirthdateChange() {

        boolean hasErrors = false;
        LocalDate birthDate = ((DatePicker) this.inputGroupBirthDate.lookup(".input")).getValue();

        // Birthdate must not be empty or blank.
        if(birthDate == null) {
            ValidationUtil.showError(this.inputGroupBirthDate);
            hasErrors = true;
        }

        // If there is no error, hiding error message.
        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupBirthDate);
        }

        return new ValidationResult<>(birthDate, hasErrors);
    }

    @FXML
    private ValidationResult<String> onPasswordChange() {

        boolean hasErrors = false;

        String password = ((PasswordField) this.inputGroupPassword.lookup(".input")).getText();
        String confirmPassword = ((PasswordField) this.inputGroupConfirmPassword.lookup(".input")).getText();

        // Password must not be empty or blank.
        if(password.isEmpty() || password.isBlank()) {
            ValidationUtil.showError(this.inputGroupPassword);
            hasErrors = true;
        }

        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupPassword);
        }

        // Password and confirmed password must be the same.
        if(!confirmPassword.equals(password)) {
            ValidationUtil.showError(this.inputGroupConfirmPassword);
            hasErrors = true;
        }

        if(!hasErrors) {
            ValidationUtil.hideErrors(this.inputGroupConfirmPassword);
        }

        return new ValidationResult<>(password, hasErrors);
    }
}
