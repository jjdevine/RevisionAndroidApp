package com.jonathandevinesoftware.revisionapp.dropbox;

import com.jonathandevinesoftware.revisionapp.database.SingleFlashCard;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DropboxService {

    static final String DBX_OAUTH_INITIATED = "dbx-oauth-initiated";
    static final String DBX_OAUTH_TOKEN = "dbx-oauth-token";

    List<String> getQAFlashCardNames();

    QAFlashCardSourceData getQAFlashCards(String flashCardName);

    List<String> getSingleFlashCardTopics();

    List<String> getSingleFlashCardFileNames(String topic);

    Optional<SingleFlashCard> getSingleFlashCard(String topic, String fileName);
}
