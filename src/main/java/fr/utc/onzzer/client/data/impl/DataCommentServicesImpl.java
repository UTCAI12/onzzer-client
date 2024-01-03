package fr.utc.onzzer.client.data.impl;

import fr.utc.onzzer.client.data.DataCommentServices;
import fr.utc.onzzer.common.dataclass.Comment;
import fr.utc.onzzer.common.services.Listenable;

import java.util.UUID;

public class DataCommentServicesImpl extends Listenable implements DataCommentServices {

    public DataCommentServicesImpl() {}
    @Override
    public void addComment(UUID uuid, Comment comment) throws Exception {}
}
