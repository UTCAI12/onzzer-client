package fr.utc.onzzer.client.common.services;

import fr.utc.onzzer.common.dataclass.Comment;

import java.util.UUID;

interface DataCommentServices {
    void addComment(UUID uuid, Comment comment) throws Exception;
}