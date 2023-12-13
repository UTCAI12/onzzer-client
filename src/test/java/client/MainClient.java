package client;

import client.main.Frame;
import client.main.MainController;
import fr.utc.onzzer.client.communication.impl.ClientCommunicationController;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.*;
import javafx.application.Application;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
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
        final DataServicesProvider dataServicesProvider = new DataServicesProvider();

        // Initializing the communication
        final ClientCommunicationController comm = new ClientCommunicationController("localhost", 8000, dataServicesProvider);

        // Connect to the server
        List<TrackLite> items = new ArrayList<>();
        final UserLite userLite = new UserLite(user.getId(), user.getUsername());
        List<Track> tracks = dataServicesProvider.getDataTrackServices().getTracks();
        tracks.stream().filter(x -> !x.getPrivateTrack()).map(Track::toTrackLite).forEach(items::add);
        System.out.println("items = " + items);
        comm.connect(userLite, items);

        final MainController controller = new MainController(dataServicesProvider, comm);

        // Set the controller in MyApp
        Frame.setController(controller);

        // Launch the application
        Application.launch(Frame.class, args);
    }

    public static void main(String[] args) throws ConnectException {
        new MainClient(args);
    }
}