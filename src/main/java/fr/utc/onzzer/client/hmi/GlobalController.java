package fr.utc.onzzer.client.hmi;

import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;

public class GlobalController {

    private DataServicesProvider dataServicesProvider = new DataServicesProvider();
    private ComServicesProvider comServicesProvider;

    public void initialize(String address, int port) {
        this.dataServicesProvider = new DataServicesProvider();
        this.comServicesProvider = new ComServicesProvider(address, port, this.dataServicesProvider);
    }

    public DataServicesProvider getDataServicesProvider() {
        return this.dataServicesProvider;
    }

    public ComServicesProvider getComServicesProvider() {
        return this.comServicesProvider;
    }
}