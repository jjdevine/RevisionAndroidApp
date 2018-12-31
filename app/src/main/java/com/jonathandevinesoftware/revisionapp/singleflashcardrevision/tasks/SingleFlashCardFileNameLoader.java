package com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks;

import android.app.Service;
import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCard;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCardDAO;

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

        SingleFlashCardDAO dao = ServiceFactory.getRevisionDatabase().singleFlashCardDAO();

        List<String> fileNames = dao.getFileNames(topic);

        //if database contains filenames, return results from DB
        if(!fileNames.isEmpty()) {
            return fileNames;
        }

        //otherwise refer to DropBox...

        fileNames = ServiceFactory.getDropboxService().getSingleFlashCardFileNames(topic);

        //populate results from DropBox into local DB
        List<SingleFlashCard> singleFlashCardList =
                fileNames.stream()
                    .map(fileName -> new SingleFlashCard(topic, fileName, null, null))
                    .collect(Collectors.toList());

        dao.insert(singleFlashCardList.toArray(new SingleFlashCard[0]));

        return fileNames;
    }

    @Override
    protected void onPostExecute(List<String> fileNames) {
        callbackConsumer.accept(fileNames);
    }
}
