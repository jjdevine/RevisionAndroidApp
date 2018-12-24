package com.jonathandevinesoftware.revisionapp.qaflashcard;

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
import com.jonathandevinesoftware.revisionapp.database.RevisionDatabase;

import java.util.List;
import java.util.Random;

public class QAFlashcardActivity extends BaseActivity {

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

        new FlashCardLoader().execute(selection);

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

    class FlashCardLoader extends AsyncTask<String, Integer, List<QAFlashCard>> {

        @Override
        protected List<QAFlashCard> doInBackground(String... strings) {
            if(strings.length != 1) {
                throw new IllegalArgumentException("Argument list size must be exactly 1");
            }
            return ServiceFactory.getRevisionDatabase().qaFlashCardDAO().getAllByTopic(strings[0]);
        }

        @Override
        protected void onPostExecute(List<QAFlashCard> qaFlashCards) {
            showMessage("qaFlashCards: " + qaFlashCards.size());
            //TODO load flashcards from dropbox and populate if not in local db
        }
    }

}
