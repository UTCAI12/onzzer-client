package fr.utc.onzzer.client.hmi;

import fr.utc.onzzer.client.communication.ComServicesProvider;
import fr.utc.onzzer.client.data.DataServicesProvider;
import fr.utc.onzzer.client.hmi.main.IHMMainServices;
import fr.utc.onzzer.client.hmi.main.impl.IHMMainServicesImpl;
import fr.utc.onzzer.client.hmi.music.services.ViewMusicServices;
import fr.utc.onzzer.client.hmi.music.services.impl.ViewMusicServicesImpl;

public class GlobalController {
    private final DataServicesProvider dataServicesProvider;
    private ComServicesProvider comServicesProvider;
    private ViewMusicServices viewMusicServices;
    private final IHMMainServices ihmMainServices;

    public GlobalController() {
        this.dataServicesProvider = new DataServicesProvider();
        this.viewMusicServices = new ViewMusicServicesImpl(this);
        this.ihmMainServices = new IHMMainServicesImpl(this);
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

    public IHMMainServices getIHMMainServices() {
        return this.ihmMainServices;
    }
}
