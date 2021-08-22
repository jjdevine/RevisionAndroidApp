package com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardSetting;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.QAFlashcardSelectActivity;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FlashCardDatabaseLoader extends AsyncTask<String, Integer, List<QAFlashCard>> {

    private String topic;
    private BiConsumer<List<QAFlashCard>, String> callbackConsumer;

    public FlashCardDatabaseLoader(BiConsumer<List<QAFlashCard>, String> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<QAFlashCard> doInBackground(String... strings) {
        if(strings.length != 1) {
            throw new IllegalArgumentException("Argument list size must be exactly 1");
        }
        topic = strings[0];
        return ServiceFactory.getRevisionDatabase().qaFlashCardDAO().getAllByTopic(strings[0]);
    }

    @Override
    protected void onPostExecute(List<QAFlashCard> qaFlashCards) {
        callbackConsumer.accept(qaFlashCards, topic);
    }
}
