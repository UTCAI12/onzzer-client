package fr.utc.onzzer.client;

import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.main.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainClient extends Application {

    private static final String APP_TITLE = "Onzzer";

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        // Global controller that contains services references.
        GlobalController controller = new GlobalController();

        // Global variable for stage.
        MainClient.stage = stage;

        // Loading the view.
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/login-view.fxml"));

        LoginViewController loginViewController = new LoginViewController(controller);
        fxmlLoader.setController(loginViewController);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}