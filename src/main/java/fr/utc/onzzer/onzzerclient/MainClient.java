package fr.utc.onzzer.onzzerclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class MainClient extends Application {

    private static final String APP_TITLE = "Onzzer";

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        MainClient.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/login-view.fxml"));
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