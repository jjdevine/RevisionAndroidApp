package com.jonathandevinesoftware.revisionapp.dropbox;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.jonathandevinesoftware.revisionapp.common.App;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DropboxServiceImpl implements DropboxService {

    private DbxClientV2 dropboxClient;

    @Override
    public List<String> getQAFlashCardNames() {

        try {
            return getDropboxClient().files()
                    .listFolder("/QAFlashCards")
                    .getEntries()
                    .stream()
                    .map(lfr -> lfr.getName())
                    .map(DropboxServiceImpl::trimFileNames)
                    .collect(Collectors.toList());
        } catch (DbxException e) {
            e.printStackTrace();
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public Map<String, String> getQAFlashCards(String flashCardName) {

        Map<String, String> flashCards = new HashMap<>();

        try {
            InputStream is = getDropboxClient().files().download("/QAFlashCards/" + flashCardName + ".txt").getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            reader.lines().forEach(line -> {
                line = line.trim();
                if(!line.startsWith("#")) {  //lines starting with hash are comments
                    String[] tokens = line.split("=");
                    if(tokens.length == 2) { //lines must be two strings separated by an equals
                        flashCards.put(tokens[0], tokens[1]);
                    }
                }
            });

        } catch (DbxException e) {
            e.printStackTrace();
        }

        return flashCards;
    }

    private DbxClientV2 getDropboxClient() {
        if(dropboxClient == null) {
            DbxRequestConfig config = new DbxRequestConfig("revisionApp", Locale.getDefault().toString());
            dropboxClient = new DbxClientV2(config, getToken().get());
        }
        return dropboxClient;
    }

    private static String trimFileNames(String fileName) {
        if(fileName.indexOf('.') != -1) {
            return fileName.substring(0, fileName.indexOf('.'));
        } else {
            return fileName;
        }
    }

    private Optional<String> getToken() {
        SharedPreferences preferences = App.getContext().getSharedPreferences("com.jonathandevinesoftware.revisionapp", Context.MODE_PRIVATE);
        return Optional.of(preferences.getString(DropboxService.DBX_OAUTH_TOKEN, null));
    }

}
