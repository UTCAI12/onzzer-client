package fr.utc.onzzer.client.hmi.main;
import java.util.Arrays;
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

        List<String> yourElementsList = Arrays.asList(
                "Élément 1", "Élément 2", "Élément 3", "Élément 4", "Élément 5",
                "Élément 6", "Élément 7", "Élément 8", "Élément 9", "Élément 10",
                "Élément 11", "Élément 12", "Élément 13", "Élément 14", "Élément 15",
                "Élément 16", "Élément 17", "Élément 18", "Élément 19", "Élément 20",
                "Élément 1", "Élément 2", "Élément 3", "Élément 4", "Élément 5",
                "Élément 6", "Élément 7", "Élément 8", "Élément 9", "Élément 10",
                "Élément 11", "Élément 12", "Élément 13", "Élément 14", "Élément 15",
                "Élément 16", "Élément 17", "Élément 18", "Élément 19", "Élément 20",
                "Élément 1", "Élément 2", "Élément 3", "Élément 4", "Élément 5",
                "Élément 6", "Élément 7", "Élément 8", "Élément 9", "Élément 10",
                "Élément 11", "Élément 12", "Élément 13", "Élément 14", "Élément 15",
                "Élément 16", "Élément 17", "Élément 18", "Élément 19", "Élément 20",
                "Élément 1", "Élément 2", "Élément 3", "Élément 4", "Élément 5",
                "Élément 6", "Élément 7", "Élément 8", "Élément 9", "Élément 10",
                "Élément 11", "Élément 12", "Élément 13", "Élément 14", "Élément 15",
                "Élément 16", "Élément 17", "Élément 18", "Élément 19", "Élément 20"
        );

        ObservableList<String> items = this.usersList.getItems();

        items.addAll(yourElementsList);

        /*// Adding a listener to get the new user connected and to add it to the list.
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
        }, UserLite.class, ModelUpdateTypes.DELETE_USER);*/

        // Removing focus on list view.
        this.usersList.setMouseTransparent(false);
        this.usersList.setFocusTraversable(true);
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
