package fr.utc.onzzer.client.hmi.main.impl;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.main.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class IHMMainServicesImpl implements IHMMainServices {

    private static final String APP_TITLE = "Onzzer";

    private final GlobalController controller;

    private final LoginViewController loginViewController;
    private final RegisterViewController registerViewController;
    private final MainViewController mainViewController;
    private final MyTrackController myTrackController;

    public IHMMainServicesImpl(GlobalController controller) {
        this.controller = controller;

        this.loginViewController = new LoginViewController(controller);
        this.registerViewController = new RegisterViewController(controller);
        this.mainViewController = new MainViewController(controller);
        this.myTrackController = new MyTrackController(controller);
    }

    @Override
    public void openApplication(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/login-view.fxml"));
        fxmlLoader.setController(this.loginViewController);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(APP_TITLE);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void openLoginView() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/login-view.fxml"));
        fxmlLoader.setController(this.loginViewController);

        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    @Override
    public void openRegisterView() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/register-view.fxml"));
        fxmlLoader.setController(this.registerViewController);

        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    @Override
    public void openMainView() throws IOException {

        Stage stage = MainClient.getStage();
        Scene current = stage.getScene();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/main-view.fxml"));
        fxmlLoader.setController(this.mainViewController);

        Scene scene = new Scene(fxmlLoader.load(), current.getWidth(), current.getHeight());

        stage.setScene(scene);
    }

    @Override
    public void openTrackList() throws IOException {

        // Get the JavaFx parent element
        Stage stage = MainClient.getStage();
        Scene scene = stage.getScene();

        BorderPane borderPane = (BorderPane) scene.getRoot();

        // Load the view and controller
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/my-track-view.fxml"));
        fxmlLoader.setController(this.myTrackController);

        // Update the displayed scene
        borderPane.setCenter(fxmlLoader.load());
    }
}
