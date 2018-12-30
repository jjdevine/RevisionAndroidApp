package com.jonathandevinesoftware.revisionapp.dropbox;

import java.util.List;
import java.util.Map;

public interface DropboxService {

    static final String DBX_OAUTH_INITIATED = "dbx-oauth-initiated";
    static final String DBX_OAUTH_TOKEN = "dbx-oauth-token";

    List<String> getQAFlashCardNames();

    Map<String, String> getQAFlashCards(String flashCardName);

    List<String> getSingleFlashCardTopics();
}
