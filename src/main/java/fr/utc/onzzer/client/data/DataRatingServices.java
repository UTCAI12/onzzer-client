package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.Rating;
import java.util.UUID;

public interface DataRatingServices {
    void addRating(UUID uuid, Rating rating) throws Exception;
}