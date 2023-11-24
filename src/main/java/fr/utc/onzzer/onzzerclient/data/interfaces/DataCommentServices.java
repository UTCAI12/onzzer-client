import onzzer-common.src.main.java.fr.utc.onzzer.common.dataclass

public interface DataCommentServices {
    void addComment(UUID uuid, Comment comment) throws Exception;
}