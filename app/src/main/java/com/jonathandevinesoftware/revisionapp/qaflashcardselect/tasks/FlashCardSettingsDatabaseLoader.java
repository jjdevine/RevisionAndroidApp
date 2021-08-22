package com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardSetting;

import java.sql.SQLOutput;
import java.util.List;
import java.util.function.Consumer;

public class FlashCardSettingsDatabaseLoader extends AsyncTask<String, Integer, List<QAFlashCardSetting>> {
    private Consumer<List<QAFlashCardSetting>> callbackConsumer;

    public FlashCardSettingsDatabaseLoader(Consumer<List<QAFlashCardSetting>> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected List<QAFlashCardSetting> doInBackground(String... strings) {
        System.out.println("FlashCardSettingsDatabaseLoader.doInBackground");
        return ServiceFactory.getRevisionDatabase().qaFlashCardSettingDAO().getAllByTopic(strings[0]);
    }

    @Override
    protected void onPostExecute(List<QAFlashCardSetting> settings) {
        callbackConsumer.accept(settings);
    }
}
