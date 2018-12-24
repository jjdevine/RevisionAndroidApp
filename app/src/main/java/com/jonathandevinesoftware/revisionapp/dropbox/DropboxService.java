package com.jonathandevinesoftware.revisionapp.dropbox;

import java.util.List;

public interface DropboxService {

    static final String DBX_OAUTH_INITIATED = "dbx-oauth-initiated";
    static final String DBX_OAUTH_TOKEN = "dbx-oauth-token";

    List<String> getQAFlashCardNames();
}
