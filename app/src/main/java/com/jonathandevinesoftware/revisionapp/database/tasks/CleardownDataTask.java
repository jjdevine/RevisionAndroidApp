package com.jonathandevinesoftware.revisionapp.database.tasks;

import android.os.AsyncTask;

import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;

import java.util.List;
import java.util.function.Consumer;

public class CleardownDataTask extends AsyncTask<String, Integer, Boolean> {

    private String topic;
    private Consumer<Boolean> callbackConsumer;

    public CleardownDataTask(Consumer<Boolean> callbackConsumer) {
        this.callbackConsumer = callbackConsumer;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        ServiceFactory.getRevisionDatabase().singleFlashCardDAO().deleteAll();
        ServiceFactory.getRevisionDatabase().qaFlashCardDAO().deleteAll();
        ServiceFactory.getRevisionDatabase().qaFlashCardSettingDAO().deleteAll();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        callbackConsumer.accept(success);
    }
}
