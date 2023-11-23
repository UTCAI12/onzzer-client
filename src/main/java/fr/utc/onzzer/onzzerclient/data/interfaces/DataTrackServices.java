import onzzer-common.src.main.java.fr.utc.onzzer.common.dataclass
public interface DataTrackServices {
    void saveTrack(Track track) throws Exception;
    void updateTrack(Track track) throws Exception;
    Track getTrack(UUID uuid) throws Exception;
    void addTrackToLibrary(UUID uuid);
    void removeAllTracks() throws Exception;
}