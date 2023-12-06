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
        this.dataUserServices = new DataUserServicesImpl(this.dataRepository);
        this.dataTrackServices = new DataTrackServicesImpl(this.dataRepository); // this.dataRepository en parametre
        this.dataRatingServices = new DataRatingServicesImpl(); // this.dataRepository en parametre
        this.dataCommentServices = new DataCommentServicesImpl(); // this.dataRepository en parametre
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
