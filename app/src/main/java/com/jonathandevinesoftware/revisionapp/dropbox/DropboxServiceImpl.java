package com.jonathandevinesoftware.revisionapp.dropbox;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.jonathandevinesoftware.revisionapp.common.App;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class DropboxServiceImpl implements DropboxService {

    @Override
    public List<String> getQAFlashCardNames() {
        DbxRequestConfig config = new DbxRequestConfig("revisionApp", Locale.getDefault().toString());
        DbxClientV2 client  = new DbxClientV2(config, getToken().get());

        try {
            return client.files()
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
