package fr.utc.onzzer.client.hmi.main;

import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.data.DataUserServices;
import fr.utc.onzzer.client.hmi.GlobalController;
import fr.utc.onzzer.common.dataclass.ModelUpdateTypes;
import fr.utc.onzzer.common.dataclass.TrackLite;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MainViewController {

    private final GlobalController controller;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> usersList;

    public MainViewController(GlobalController controller) {
        this.controller = controller;
    }

    public void initialize() {

        // Initializing the user list.
        this.initializeUserList();
    }

    private void initializeUserList() {

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        // Refresh the user list with the connected users.
        this.refreshUsersList();

        // Adding a listener to get the new user connected and to add it to the list.
        dataUserServices.addListener(user -> {
            Platform.runLater(() -> {
                this.usersList.getItems().add(user.getUsername());
            });
        }, UserLite.class, ModelUpdateTypes.NEW_USER);

        // Adding a listener to get the user disconnected and remove it from the list.
        dataUserServices.addListener(user -> {
            Platform.runLater(() -> {
                this.usersList.getItems().remove(user.getUsername());
            });
        }, UserLite.class, ModelUpdateTypes.DELETE_USER);
    }

    private void refreshUsersList() {

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        Map<UserLite, List<TrackLite>> connectedUsers = dataUserServices.getConnectedUsers();
        Collection<UserLite> users = connectedUsers.keySet();

        ObservableList<String> items = this.usersList.getItems();
        items.clear();

        users.forEach(user -> items.add(user.getUsername()));
    }

    @FXML
    private void handleSearch() {

        DataServicesProvider dataServicesProvider = this.controller.getDataServicesProvider();
        DataUserServices dataUserServices = dataServicesProvider.getDataUserServices();

        // Filtering the list using the user input.
        String text = this.searchField.getText().toLowerCase();

        // Refreshing the whole list.
        if (text.isEmpty()) {
            this.refreshUsersList();
            return;
        }

        // Updating the list.
        Map<UserLite, List<TrackLite>> users = dataUserServices.getConnectedUsers();

        ObservableList<String> items = this.usersList.getItems();
        items.clear();

        users.keySet().stream()
                .filter(user -> user.getUsername().toLowerCase().contains(text))
                .forEach(user -> items.add(user.getUsername()));
    }
}
