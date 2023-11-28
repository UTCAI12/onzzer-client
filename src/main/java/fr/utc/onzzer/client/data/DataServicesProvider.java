package fr.utc.onzzer.client.data;

import fr.utc.onzzer.client.data.impl.*;

public class DataServicesProvider {

    private DataRepository dataRepository;

    private DataUserServices dataUserServices;

    private DataTrackServices dataTrackServices;

    private DataRatingServices dataRatingServices;

    private DataCommentServices dataCommentServices;

    public DataServicesProvider() {

        this.dataRepository = new DataRepository();
        this.dataUserServices = new DataUserServicesImpl();
        this.dataTrackServices = new DataTrackServicesImpl();
        this.dataRatingServices = new DataRatingServicesImpl();
        this.dataCommentServices = new DataCommentServicesImpl();
    }

    public DataRepository getDataRepository() {
        return this.dataRepository;
    }

    public DataUserServices getDataUserServices() {
        return this.dataUserServices;
    }

    public DataTrackServices getDataTrackServices() {
        return this.dataTrackServices;
    }

    public DataRatingServices getDataRatingServices() {
        return this.dataRatingServices;
    }

    public DataCommentServices getDataCommentService() {
        return this.dataCommentServices;
    }
}
