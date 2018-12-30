package com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardDAO;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.QAFlashcardSelectActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FlashCardDropBoxLoader extends AsyncTask<String, Integer, List<QAFlashCard>> {
    private String topic;
    private BiConsumer<List<QAFlashCard>, String> callbackConsumer;

    public FlashCardDropBoxLoader(BiConsumer<List<QAFlashCard>, String> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<QAFlashCard> doInBackground(String... strings) {
        if(strings.length != 1) {
            throw new IllegalArgumentException("Argument list size must be exactly 1");
        }
        topic = strings[0];

        //load flashcards and insert them into the db
        Map<String, String> dropBoxFlashCards = ServiceFactory.getDropboxService().getQAFlashCards(topic);

        QAFlashCardDAO dao = ServiceFactory.getRevisionDatabase().qaFlashCardDAO();
        List<QAFlashCard> qaFlashCardList = new ArrayList<>();

        dropBoxFlashCards.keySet().stream().forEach(key -> {
            qaFlashCardList.add(new QAFlashCard(topic, key, dropBoxFlashCards.get(key), false));
        });

        dao.insert(qaFlashCardList.toArray(new QAFlashCard[0]));

        return qaFlashCardList;
    }

    @Override
    protected void onPostExecute(List<QAFlashCard> qaFlashCards) {
      callbackConsumer.accept(qaFlashCards, topic);
    }
}
