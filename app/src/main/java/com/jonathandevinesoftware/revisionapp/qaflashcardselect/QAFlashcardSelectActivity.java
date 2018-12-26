package com.jonathandevinesoftware.revisionapp.qaflashcardselect;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.widget.Button;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardDAO;
import com.jonathandevinesoftware.revisionapp.database.RevisionDatabase;
import com.jonathandevinesoftware.revisionapp.qaflashcardrevision.QAFlashCardRevisionActivity;
import com.jonathandevinesoftware.revisionapp.qaflashcardrevision.QAFlashCardsWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QAFlashcardSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaflashcard);
        new FlashCardNameLoader().execute();
    }

    private void addButtons(List<String> names) {

        Button lastButton = null;

        for(String name: names) {
            lastButton = addButton(name, lastButton);
        }
    }

    private Button addButton(String title, Button lastButton) {

        ConstraintLayout layout = findViewById(R.id.qaFlashcardButtonLayout);
        ConstraintSet constraintSet = new ConstraintSet();

        Button newButton = new Button(this);
        newButton.setText(title);
        newButton.setHeight(50);
        newButton.setId(new Random().nextInt(99999999));

        layout.addView(newButton);

        constraintSet.clone(layout);

        View connectedTo = layout;
        int connectedEdge = ConstraintSet.TOP;

        if(lastButton != null) {
            connectedTo = lastButton;
            connectedEdge = ConstraintSet.BOTTOM;
        }

        //set contraints for new button
        constraintSet.connect(newButton.getId(), ConstraintSet.TOP, connectedTo.getId(), connectedEdge, 8);
        constraintSet.connect(newButton.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 8);
        constraintSet.connect(newButton.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        constraintSet.constrainWidth(newButton.getId(), ConstraintSet.MATCH_CONSTRAINT);
        constraintSet.applyTo(layout);

        newButton.setOnClickListener(view -> onFlashcardSelection(title));

        return newButton;
    }

    public void onFlashcardSelection(String selection) {

        RevisionDatabase revisionDatabase = ServiceFactory.getRevisionDatabase();

        new FlashCardDatabaseLoader().execute(selection);

        System.out.println(selection);
    }

    class FlashCardNameLoader extends AsyncTask <String, Integer, List<String>> {

        @Override
        protected List<String> doInBackground(String... strings) {
            return ServiceFactory.getDropboxService().getQAFlashCardNames();
        }

        @Override
        protected void onPostExecute(List<String> flashCardNames) {
            addButtons(flashCardNames);
        }
    }

    class FlashCardDatabaseLoader extends AsyncTask<String, Integer, List<QAFlashCard>> {

        String topic;

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
            if(qaFlashCards.size() == 0 ) {
                System.out.println("No flashcards stored locally, loading from DropBox...");
                new FlashCardDropboxLoader().execute(topic);
            } else {
                System.out.println(qaFlashCards.size() + " Flashcards loaded from local database");
                onQAFlashCardsLoaded(topic, qaFlashCards);
            }
        }
    }

    class FlashCardDropboxLoader extends AsyncTask<String, Integer, List<QAFlashCard>> {
        String topic;

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
                qaFlashCardList.add(new QAFlashCard(topic, key, dropBoxFlashCards.get(key)));
            });

            dao.insert(qaFlashCardList.toArray(new QAFlashCard[0]));

            return qaFlashCardList;
        }

        @Override
        protected void onPostExecute(List<QAFlashCard> qaFlashCards) {
            if(qaFlashCards.size() == 0 ) {
                showMessage("Unable to load flashcards from device or DropBox");
            } else {
                System.out.println(qaFlashCards.size() + " Flashcards loaded from dropbox");
                onQAFlashCardsLoaded(topic, qaFlashCards);
            }
        }
    }

    private void onQAFlashCardsLoaded(String topic, List<QAFlashCard> qaFlashCardList) {
        qaFlashCardList.forEach(System.out::println);
        Intent intent = new Intent(this, QAFlashCardRevisionActivity.class);
        intent.putExtra("flashCards", new QAFlashCardsWrapper(topic, qaFlashCardList));
        startActivity(intent);
    }

}
