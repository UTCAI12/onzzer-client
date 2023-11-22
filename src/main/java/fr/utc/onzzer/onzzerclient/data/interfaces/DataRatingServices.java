import onzzer-common.src.main.java.fr.utc.onzzer.common.dataclass

interface DataRatingServices {
    void addRating(UUID uuid, Rating rating) throws Exception;
}