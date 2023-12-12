package fr.utc.onzzer.client.data;

import fr.utc.onzzer.common.dataclass.Comment;
import fr.utc.onzzer.common.services.Service;

import java.util.UUID;

public interface DataCommentServices extends Service {
    void addComment(UUID uuid, Comment comment) throws Exception;
}