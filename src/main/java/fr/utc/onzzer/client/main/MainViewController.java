package fr.utc.onzzer.client.main;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.ClientModel;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class MainViewController {


    @FXML
    private ListView<String> usersList;


    @FXML
    public TextField searchField;

    private ClientModel model;

    private ObservableList<String> usernamesList = FXCollections.observableArrayList("Ilian", "Clement", "Matthieu");

    // If the model triggers an "NEW_USER" event, call the "updateAllUsers" method
    //this.model.addListener(newUser -> this.UpdateUsersList(newUser.toString,true), UserLite.class, ModelUpdateTypes.NEW_USER);

    // If the model triggers an "DELETE_USER" event, call the "updateAllUsers" method
    //this.model.addListener(newUser -> this.UpdateUsersList(newUser.toString,false), UserLite.class, ModelUpdateTypes.DELETE_USER);


    private void UpdateUsersList(String user,boolean IsAddUser)
    {
        this.usersList.getItems().clear();
        //this.model.others.forEach(user -> this.usersList.getItems().add(user.getName()));

        if(IsAddUser)
        {
            usernamesList.add(user);
        }
        else
        {
            usernamesList.remove(user);
        }

        for (String element : usernamesList) {
            this.usersList.getItems().add(element);
        }
    }

    @FXML
    private void handleSearch()
    {
        ObservableList<String> filteredList = usernamesList.filtered(item -> item.toLowerCase().contains(searchField.getText().toLowerCase()));

        this.usersList.getItems().clear();
        // Mettre à jour la liste affichée dans la vue
        for (String element : filteredList) {
            this.usersList.getItems().add(element);
        }

    }


    public void initialize() {
        System.out.println("coucou");
        for (String element : usernamesList) {
            this.usersList.getItems().add(element);
        }

    }
}
