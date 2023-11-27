package fr.utc.onzzer.client.communication;

import fr.utc.onzzer.client.communication.impl.ClientCommunicationController;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.common.dataclass.User;
import fr.utc.onzzer.common.dataclass.UserLite;

public class ComServicesProvider {

    private final ComMusicServices comMusicServices;

    private final ComMainServices comMainServices;

    public ComServicesProvider(final String serverAddress, int serverPort, final DataServicesProvider dataServicesProvider) {
        final ClientCommunicationController clientCommunicationController = new ClientCommunicationController(serverAddress, serverPort, dataServicesProvider);

        this.comMusicServices = clientCommunicationController;
        this.comMainServices = clientCommunicationController;
    }

    public ComMusicServices getComMusicServices() {
        return comMusicServices;
    }

    public ComMainServices getComMainServices() {
        return comMainServices;
    }
}
