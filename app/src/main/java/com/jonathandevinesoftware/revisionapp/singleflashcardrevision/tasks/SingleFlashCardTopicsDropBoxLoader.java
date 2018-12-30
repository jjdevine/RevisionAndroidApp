package com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;

import java.util.List;
import java.util.function.Consumer;

public class SingleFlashCardTopicsDropBoxLoader extends AsyncTask<String, Integer, List<String>> {

    private Consumer<List<String>> callbackConsumer;

    public SingleFlashCardTopicsDropBoxLoader(Consumer<List<String>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        return ServiceFactory.getDropboxService().getSingleFlashCardTopics();
    }

    @Override
    protected void onPostExecute(List<String> topics) {
        callbackConsumer.accept(topics);
    }
}
