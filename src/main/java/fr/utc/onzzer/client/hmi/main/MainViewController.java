package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class MainViewController {

    private List<UserLite> usersConnected;
    private final GlobalController controller;
    @FXML
    public TextField searchField;

    @FXML
    private ListView<String> usersList;
    private ClientModel model;
    private final ObservableList<String> usernamesList = FXCollections.observableArrayList("Ilian", "Clement", "Matthieu");

    public MainViewController(GlobalController controller) {
        this.controller = controller;
    }

    // If the model triggers an "NEW_USER" event, call the "updateAllUsers" method
    //this.model.addListener(newUser -> this.UpdateUsersList(newUser.toString,true), UserLite.class, ModelUpdateTypes.NEW_USER);

    // If the model triggers an "DELETE_USER" event, call the "updateAllUsers" method
    //this.model.addListener(newUser -> this.UpdateUsersList(newUser.toString,false), UserLite.class, ModelUpdateTypes.DELETE_USER);

    private void UpdateUsersList(String user, boolean IsAddUser) {
        this.usersList.getItems().clear();
        //this.model.others.forEach(user -> this.usersList.getItems().add(user.getName()));

        if (IsAddUser) {
            usernamesList.add(user);
        } else {
            usernamesList.remove(user);
        }

        for (String element : usernamesList) {
            this.usersList.getItems().add(element);
        }
    }

    @FXML
    private void handleSearch() {
        ObservableList<String> filteredList = usernamesList.filtered(item -> item.toLowerCase().contains(searchField.getText().toLowerCase()));

        this.usersList.getItems().clear();
        // Mettre à jour la liste affichée dans la vue
        for (String element : filteredList) {
            this.usersList.getItems().add(element);
        }

    }


    public void initialize() {

        //usersConnected = dataUserServices.getConnectedUsers();
/*        System.out.println("coucou");
        for (UserLite element : usersConnected) {
            this.usersList.getItems().add(element.getUsername());
        }*/

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        // Adding a listener to get the new user connected
       dataUserServices.addListener(user -> {
            Platform.runLater(() -> {
                try {
                    this.usersList.getItems().add(user.getUsername());

                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        }, UserLite.class, ModelUpdateTypes.NEW_USER);

        // Adding a listener to get the new user connected
        dataUserServices.addListener(user -> {
            Platform.runLater(() -> {
                try {
                    this.usersList.getItems().remove(user.getUsername());
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            });
        }, UserLite.class, ModelUpdateTypes.DELETE_USER);


    }
}
