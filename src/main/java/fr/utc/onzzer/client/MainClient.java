package fr.utc.onzzer.client;

import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.main.IHMMainServices;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class MainClient extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {

        // Global controller that contains services references.
        GlobalController controller = new GlobalController();

        // Global variable for stage.
        MainClient.stage = stage;

        // Loading the view.
        IHMMainServices services = controller.getIHMMainServices();
        services.openApplication(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    public static Stage getStage() {
        return stage;
    }
}