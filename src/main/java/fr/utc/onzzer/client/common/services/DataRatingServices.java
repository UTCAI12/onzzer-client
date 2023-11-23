package fr.utc.onzzer.client.common.services;

import fr.utc.onzzer.common.dataclass.Rating;
import java.util.UUID;

interface DataRatingServices {
    void addRating(UUID uuid, Rating rating) throws Exception;
}