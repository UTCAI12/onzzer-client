package fr.utc.onzzer.client.hmi.music.services.impl;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.main.LoginViewController;

import fr.utc.onzzer.client.hmi.music.SearchViewController;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.UUID;

public class ViewMusicServicesImpl implements ViewMusicServices {
    private final GlobalController globalController;

    public ViewMusicServicesImpl(GlobalController globalController) {
        this.globalController = globalController;
    }

    @Override
    public void openSearchTracks(Scene scene) throws IOException {
        // Get the JavaFx parent element
        BorderPane borderPane = (BorderPane) scene.getRoot();

        // Load the view and controller
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/search-view.fxml"));
        SearchViewController searchViewController = new SearchViewController(globalController);
        fxmlLoader.setController(searchViewController);

        // Update the displayed scene
        borderPane.setCenter(fxmlLoader.load());
    }

    @Override
    public void openCreateTrack() throws IOException {
        LoginViewController controller = new LoginViewController(globalController);
        openInModal("Nouveau morceau", "/fxml/create-track-view.fxml", controller);
    }

    @Override
    public void openEditTrack(UUID trackId) throws IOException {
        LoginViewController controller = new LoginViewController(globalController);
        openInModal("Modifier un morceau", "/fxml/edit-track-view.fxml", controller);
    }

    @Override
    public void openDeleteTrack(UUID trackId) throws IOException {
        LoginViewController controller = new LoginViewController(globalController);
        openInModal("Supprimer un morceau", "/fxml/delete-track-view.fxml", controller);
    }

    @Override
    public void openMediaPlayer(Scene scene, UUID trackId) throws IOException {
        // Get the JavaFx parent element
        BorderPane borderPane = (BorderPane) scene.getRoot();

        // Load the view and controller
        // TODO change the test view and controller
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/listen-view.fxml"));
        //LoginViewController loginViewController = new LoginViewController(new GlobalController());
        //fxmlLoader.setController(loginViewController);

        // Update the displayed scene
        borderPane.setCenter(fxmlLoader.load());
    }

    private void openInModal(String title, String viewPath, Object controller) throws IOException {
        // Create a new Stage / Window for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL); // Block the background
        popupStage.initStyle(StageStyle.UTILITY); // Only close button
        popupStage.setTitle(title);

        // Load the view and controller
        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource(viewPath));
        fxmlLoader.setController(controller);

        // Set the content of the popup stage
        Scene popupScene = new Scene(fxmlLoader.load(), 200, 100);
        popupStage.setScene(popupScene);

        // Set the position relative to the primary stage
        //popupStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - popupStage.getWidth() / 2);
        //popupStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - popupStage.getHeight() / 2);

        // Show the popup
        popupStage.showAndWait();
    }
}
