package fr.utc.onzzer.client.hmi.main;

import javafx.stage.Stage;

import java.io.IOException;

public interface IHMMainServices {

    void openApplication(Stage stage) throws IOException;

    void openLoginView() throws IOException;

    void openRegisterView() throws IOException;

    void openMainView() throws IOException;

    void openTrackList() throws IOException;
}
