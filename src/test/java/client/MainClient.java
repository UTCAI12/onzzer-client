package client;

import client.main.Frame;
import client.main.MainController;
import fr.utc.onzzer.client.common.communication.ClientCommunicationController;
import fr.utc.onzzer.common.dataclass.ClientModel;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;
import javafx.application.Application;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


public class MainClient {
    public MainClient(String[] args) throws ConnectException {
        String[] names = {
                "Ilian",
                "Clement",
                "Myriem",
                "Gwendal",
                "Matthieu",
                "Cecile",
                "Mostafa",
                "Sophie",
                "Riyad",
                "Valentin",
                "Edouard",
                "Thomas" };
        Random random = new Random();
        int index = random.nextInt(names.length);

        // Initializing a user with a random name
        final User user = new User(UUID.randomUUID(), names[index], "mail", "password");

        // Initializing the Model
        final ClientModel m = new ClientModel(user);

        // Initializing the communication
        final ClientCommunicationController comm = new ClientCommunicationController("localhost", 8000, m);

        // Connect to the server

        // TODO !!! User doit peut etre hériter de UserLite
        final UserLite userLite = new UserLite(user.getId(), user.getUsername());
        comm.connect(userLite, new ArrayList<>());

        final MainController controller = new MainController(m, comm);

        // Set the controller in MyApp
        Frame.setController(controller);

        // Launch the application
        Application.launch(Frame.class, args);
    }

    public static void main(String[] args) throws ConnectException {
        new MainClient(args);
    }
}