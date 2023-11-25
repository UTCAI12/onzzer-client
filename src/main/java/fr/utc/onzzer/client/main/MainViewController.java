package fr.utc.onzzer.client.main;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.ClientModel;

public class MainViewController {

    @FXML
    private ListView<String> usersList;

    private final ClientModel model;

    // If the model triggers an "NEW_USER" event, call the "updateAllUsers" method
    this.model.addListener(newUser -> this.UpdateUsersList(), UserLite.class, ModelUpdateTypes.NEW_USER);

    // If the model triggers an "DELETE_USER" event, call the "updateAllUsers" method
    this.model.addListener(newUser -> this.UpdateUsersList(), UserLite.class, ModelUpdateTypes.DELETE_USER);


    private void UpdateUsersList()
    {
        this.usersList.getItems().clear();
        this.model.others.forEach(user -> this.usersList.getItems().add(user.getName()));

    }

    public void initialize() {
        System.out.println("coucou");

    }
}
