package com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;

import java.util.List;
import java.util.function.Consumer;

public class FlashCardTopicsDatabaseLoader extends AsyncTask<String, Integer, List<String>> {
    private Consumer<List<String>> callbackConsumer;

    public FlashCardTopicsDatabaseLoader(Consumer<List<String>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<String> doInBackground(String... strings) {
        return ServiceFactory.getRevisionDatabase().qaFlashCardDAO().getAllTopics();
    }

    @Override
    protected void onPostExecute(List<String> topics) {
        callbackConsumer.accept(topics);
    }
}
