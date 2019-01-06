package com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.App;
import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class SingleFlashCardTopicsLoader extends AsyncTask<String, Integer, List<String>> {

    private Consumer<List<String>> callbackConsumer;

    public SingleFlashCardTopicsLoader(Consumer<List<String>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<String> doInBackground(String... strings) {

        List<String> topics = ServiceFactory.getRevisionDatabase().singleFlashCardDAO().getTopics();
        System.out.println(topics.size() + " topic(s) found in local db...");

        if(!App.hasNetworkAccess()) {
            System.out.println("No network access, return local topics only");
            return topics;
        }

        System.out.println("loading topics from dropbox...");
        List<String> dropBoxTopics = ServiceFactory.getDropboxService().getSingleFlashCardTopics();
        System.out.println(dropBoxTopics.size() + " topic(s) found in DropBox...");

        Set<String> combinedTopics = new HashSet<>();

        combinedTopics.addAll(topics);
        combinedTopics.addAll(dropBoxTopics);

        List<String> result = new ArrayList<>();
        result.addAll(combinedTopics);
        System.out.println("Returning " + result.size() + " topic(s)...");

        return result;
    }

    @Override
    protected void onPostExecute(List<String> topics) {
        callbackConsumer.accept(topics);
    }
}
