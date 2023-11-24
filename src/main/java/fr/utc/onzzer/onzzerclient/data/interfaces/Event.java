import onzzer-common.src.main.java.fr.utc.onzzer.common.dataclass

public interface EVENTS {
    void NEW_USER(User user);

    void UPDATE_USER(User user);

    void DELETE_USER(UserLite userLite);

    void TRACK_READY_DOWNLOAD(Track track);

    void UPDATE_TRACK(Track track);

    void NEW_TRACK(TrackLite trackLite);

    void DELETE_TRACK(TrackLite trackLite);

    void DOWNLOADED_TRACK(Track track);

    void TRACK_READY_PLAY(Track track);

    void NEW_RATING(Rating rating);

    void REMOVE_ALL();

    void NEW_COMMENT(Comment comment);
}
