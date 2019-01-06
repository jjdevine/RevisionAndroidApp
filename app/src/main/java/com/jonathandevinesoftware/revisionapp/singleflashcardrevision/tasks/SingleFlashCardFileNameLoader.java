package com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks;

import android.app.Service;
import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.App;
import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCard;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCardDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * loads file names for a flash card based on the topic (directory) name.
 * if the file names are already stored in the db, return these. otherwise
 * go to dropbox to load the file names and populate them into the db before
 * returning.
 */
public class SingleFlashCardFileNameLoader extends AsyncTask<String, Integer, List<String>> {

    private Consumer<List<String>> callbackConsumer;

    public SingleFlashCardFileNameLoader(Consumer<List<String>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        if(strings.length != 1) {
            throw new IllegalArgumentException("Argument list must be of length 1");
        }
        String topic = strings[0];

        if(!App.hasNetworkAccess()) {
            System.out.println("Offline - only returning locally stored flashcard names");
            //only return flashcards fully stored in local db
            return ServiceFactory.getRevisionDatabase().singleFlashCardDAO().getFileNamesForCompleteFlashCards(topic);
        }

        SingleFlashCardDAO dao = ServiceFactory.getRevisionDatabase().singleFlashCardDAO();

        List<String> fileNames = dao.getFileNames(topic);
        System.out.println(fileNames.size() + " flashcard(s) loaded from db");

        //check any additions from DropBox ...
        List<String> dropBoxFileNames = ServiceFactory.getDropboxService().getSingleFlashCardFileNames(topic);
        List<SingleFlashCard> dropBoxFlashCards = new ArrayList<>();

        for(String dropBoxFileName : dropBoxFileNames) {
            if(fileNames.contains(dropBoxFileName)) {
               continue; //skip any that already exist in local db
            }

            //add this flashcard to db
            dropBoxFlashCards.add(new SingleFlashCard(topic, dropBoxFileName, null, null));

            //add to list of names to return
            fileNames.add(dropBoxFileName);
        }

        if(dropBoxFlashCards.size() > 0) {
            dao.insert(dropBoxFlashCards.toArray(new SingleFlashCard[0]));
        }

        System.out.println(fileNames.size() + " flashcard(s) loaded in total");

        return fileNames;
    }

    @Override
    protected void onPostExecute(List<String> fileNames) {
        callbackConsumer.accept(fileNames);
    }
}
