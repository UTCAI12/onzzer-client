package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataRatingServices;
import fr.utc.onzzer.common.dataclass.Rating;
import fr.utc.onzzer.common.services.Listenable;

import java.util.UUID;

public class DataRatingServicesImpl extends Listenable implements DataRatingServices {

    public DataRatingServicesImpl() {}
    @Override
    public void addRating(UUID uuid, Rating rating) throws Exception {

    }
}
