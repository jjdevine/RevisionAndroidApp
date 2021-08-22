package com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardDAO;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardSetting;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardSettingDAO;
import com.jonathandevinesoftware.revisionapp.dropbox.QAFlashCardSourceData;
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
        QAFlashCardSourceData qaFlashCardSourceData = ServiceFactory.getDropboxService().getQAFlashCards(topic);

        /*
            process flashcards
         */
        QAFlashCardDAO flashCardDAO = ServiceFactory.getRevisionDatabase().qaFlashCardDAO();
        List<QAFlashCard> qaFlashCardList = new ArrayList<>();

        Map<String, String> flashCards = qaFlashCardSourceData.getFlashCards();

        flashCards.keySet().stream().forEach(key -> {
            qaFlashCardList.add(new QAFlashCard(topic, key, flashCards.get(key), false));
        });

        flashCardDAO.insert(qaFlashCardList.toArray(new QAFlashCard[0]));

        /*
            process settings
         */

        QAFlashCardSettingDAO settingDao = ServiceFactory.getRevisionDatabase().qaFlashCardSettingDAO();
        List<QAFlashCardSetting> qaFlashCardSettingList = new ArrayList<>();

        Map<String, String> flashCardSettings = qaFlashCardSourceData.getSettings();

        flashCardSettings.keySet().stream().forEach(key -> {
            qaFlashCardSettingList.add(new QAFlashCardSetting(topic, key, flashCardSettings.get(key)));
        });

        settingDao.insert(qaFlashCardSettingList.toArray(new QAFlashCardSetting[0]));

        return qaFlashCardList;
    }

    @Override
    protected void onPostExecute(List<QAFlashCard> qaFlashCards) {
      callbackConsumer.accept(qaFlashCards, topic);
    }
}
