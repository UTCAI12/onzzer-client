package fr.utc.onzzer.client.hmi.music.services;

import fr.utc.onzzer.client.MainClient;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.client.hmi.main.LoginViewController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.UUID;

public class ViewMusicServicesImpl implements ViewMusicServices {
    public ViewMusicServicesImpl(){}
    @Override
    public void openSearchTracks(Scene scene) throws IOException {
        // get borderpane
        // get searchview
        // set center

        BorderPane borderPane = (BorderPane) scene.getRoot();

        FXMLLoader fxmlLoader = new FXMLLoader(MainClient.class.getResource("/fxml/login-view.fxml"));
        LoginViewController loginViewController = new LoginViewController(new GlobalController());
        fxmlLoader.setController(loginViewController);

        borderPane.setCenter(fxmlLoader.load());
    }

    @Override
    public void openCreateTrack(){
        // Create a new stage for the popup
        Stage popupStage = new Stage();
        // put the pop-up on the foreground and block the background
        popupStage.initModality(Modality.APPLICATION_MODAL);
        // there is no close / minimize / resize button
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("Custom Popup");

        // Create the content of the popup
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> popupStage.close());

        StackPane popupLayout = new StackPane();
        popupLayout.getChildren().addAll(closeButton);
        StackPane.setAlignment(closeButton, Pos.BOTTOM_CENTER);

        Scene popupScene = new Scene(popupLayout, 200, 100);

        // Set the content of the popup stage
        popupStage.setScene(popupScene);

        // Set the position relative to the primary stage
        // popupStage.setX(primaryStage.getX() + primaryStage.getWidth() / 2 - popupStage.getWidth() / 2);
        // popupStage.setY(primaryStage.getY() + primaryStage.getHeight() / 2 - popupStage.getHeight() / 2);

        // Show the popup
        popupStage.showAndWait();
    }
    @Override
    public void openEditTrack(UUID trackId){}
    @Override
    public void openDeleteTrack(UUID trackId){}
    @Override
    public void openMediaPlayer(Scene scene, UUID trackId){}

}
