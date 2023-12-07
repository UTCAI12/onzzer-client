package fr.utc.onzzer.client.hmi;

import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import fr.utc.onzzer.client.hmi.music.services.impl.ViewMusicServicesImpl;

public class GlobalController {
    private DataServicesProvider dataServicesProvider = new DataServicesProvider();
    private ComServicesProvider comServicesProvider;
    private ViewMusicServices viewMusicServices;

    public GlobalController() {
        this.dataServicesProvider = new DataServicesProvider();
        this.viewMusicServices = new ViewMusicServicesImpl(this);
    }

    public void initialize(String address, int port) {
        this.comServicesProvider = new ComServicesProvider(address, port, this.dataServicesProvider);
    }

    public DataServicesProvider getDataServicesProvider() {
        return this.dataServicesProvider;
    }

    public ComServicesProvider getComServicesProvider() {
        return this.comServicesProvider;
    }

    public ViewMusicServices getViewMusicServices() {
        return viewMusicServices;
    }
}
