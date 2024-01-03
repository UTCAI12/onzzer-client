package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.Rating;
import fr.utc.onzzer.common.services.Service;

import java.util.UUID;

public interface DataRatingServices extends Service {
    void addRating(UUID uuid, Rating rating) throws Exception;
}