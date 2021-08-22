package com.jonathandevinesoftware.revisionapp.dropbox;

import android.content.Context;
import android.content.SharedPreferences;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.jonathandevinesoftware.revisionapp.common.App;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCard;

import java.io.BufferedReader;
import java.io.IOException;
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

    public QAFlashCardSourceData getQAFlashCards(String flashCardName) {

        Map<String, String> flashCards = new HashMap<>();
        Map<String, String> settings = new HashMap<>();
        QAFlashCardSourceData qaFlashCardSourceData = new QAFlashCardSourceData();
        qaFlashCardSourceData.setFlashCards(flashCards);
        qaFlashCardSourceData.setSettings(settings);

        try {
            InputStream is = getDropboxClient().files().download("/QAFlashCards/" + flashCardName + ".txt").getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            reader.lines().forEach(line -> {
                line = line.trim();
                if(line.startsWith("#SETTING-")) {  //the format for settings is "#SETTING-SETTINGNAME=SETTINGVALUE
                    String[] tokens = line.split("=");
                    if(tokens.length == 2) { //lines must be two strings separated by an equals
                        settings.put(tokens[0].substring(9), tokens[1]);
                    }
                } else if(!line.startsWith("#")) {  //lines starting with hash are comments
                    String[] tokens = line.split("=");
                    if(tokens.length == 2) { //lines must be two strings separated by an equals
                        flashCards.put(tokens[0], tokens[1]);
                    }
                }
            });

        } catch (DbxException e) {
            e.printStackTrace();
        }

        return qaFlashCardSourceData;
    }

    @Override
    public List<String> getSingleFlashCardTopics() {
        try {
            return getDropboxClient().files()
                    .listFolder("/SingleFlashcards")
                    .getEntries()
                    .stream()
                    .filter(metadata -> metadata instanceof FolderMetadata)
                    .map(metadata -> metadata.getName())
                    .collect(Collectors.toList());
        } catch (DbxException e) {
            e.printStackTrace();
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public List<String> getSingleFlashCardFileNames(String topic) {
        try {
            return getDropboxClient().files()
                    .listFolder("/SingleFlashcards/" + topic)
                    .getEntries()
                    .stream()
                    .filter(metadata -> metadata instanceof FileMetadata)
                    .map(metadata -> metadata.getName())
                    .collect(Collectors.toList());
        } catch (DbxException e) {
            e.printStackTrace();
        }

        return Collections.EMPTY_LIST;
    }

    @Override
    public Optional<SingleFlashCard> getSingleFlashCard(String topic, String fileName) {

        try {
            InputStream is = getDropboxClient().files().download("/SingleFlashcards/" + topic + "/" + fileName).getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder result = new StringBuilder();

            String line = null;

            String title = "noTitle";
            String mainText = "noMainText";

            int lineIndex = 0;
            while((line = reader.readLine()) != null) {
                if(lineIndex == 0) {
                    title = line;
                } else if(lineIndex >= 2) {
                    result.append(line + "\n");
                }
                lineIndex++;
            }

            return Optional.of(new SingleFlashCard(topic, fileName, title, result.toString()));

        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
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
