package fr.utc.onzzer.client.data;

import fr.utc.onzzer.client.data.impl.DataCommentServicesImpl;
import fr.utc.onzzer.client.data.impl.DataRatingServicesImpl;
import fr.utc.onzzer.client.data.impl.DataTrackServicesImpl;
import fr.utc.onzzer.client.data.impl.DataUserServicesImpl;

public class DataServicesProvider {

    private DataRepository dataRepository;

    private DataUserServices dataUserServices;

    private DataTrackServices dataTrackServices;

    private DataRatingServices dataRatingServices;

    private DataCommentServices dataCommentService;

    public DataServicesProvider() {

        this.dataRepository = new DataRepository();
        this.dataUserService = new DataUserServicesImpl();
        this.dataTrackService = new DataTrackServicesImpl();
        this.dataRatingService = new DataRatingServicesImpl();
        this.dataCommentService = new DataCommentServicesImpl();
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public DataUserServices getDataUserServices() {
        return dataUserServices;
    }

    public DataTrackServices getDataTrackServices() {
        return dataTrackServices;
    }

    public DataRatingServices getDataRatingServices() {
        return dataRatingServices;
    }

    public DataCommentServices getDataCommentService() {
        return dataCommentServices;
    }
}
