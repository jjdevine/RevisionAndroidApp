package com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.QAFlashcardSelectActivity;

import java.util.List;
import java.util.function.Consumer;

public class FlashCardTopicsDropBoxLoader extends AsyncTask<String, Integer, List<String>> {

    private Consumer<List<String>> callbackConsumer;

    public FlashCardTopicsDropBoxLoader(Consumer<List<String>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        return ServiceFactory.getDropboxService().getQAFlashCardNames();
    }

    @Override
    protected void onPostExecute(List<String> topics) {
        callbackConsumer.accept(topics);
    }
}
