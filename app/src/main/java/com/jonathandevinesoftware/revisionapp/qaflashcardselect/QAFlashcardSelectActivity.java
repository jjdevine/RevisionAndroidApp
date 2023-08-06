package com.jonathandevinesoftware.revisionapp.qaflashcardselect;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import android.view.View;
import android.widget.Button;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardSetting;
import com.jonathandevinesoftware.revisionapp.database.RevisionDatabase;
import com.jonathandevinesoftware.revisionapp.qaflashcardrevision.QAFlashCardRevisionActivity;
import com.jonathandevinesoftware.revisionapp.qaflashcardrevision.QAFlashCardsWrapper;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks.FlashCardDatabaseLoader;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks.FlashCardDropBoxLoader;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks.FlashCardSettingsDatabaseLoader;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks.FlashCardTopicsDatabaseLoader;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.tasks.FlashCardTopicsDropBoxLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QAFlashcardSelectActivity extends BaseActivity {

    private List<String> loadedTopics = new ArrayList<>();
    private Button lastButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaflashcard);

        new FlashCardTopicsDatabaseLoader(this::onFlashCardNameDatabaseLoaded).execute();
    }

    private void onFlashCardNameDatabaseLoaded(List<String> topics) {
        loadedTopics.addAll(topics);
        refreshButtons();
        loadedTopics.forEach(topic -> System.out.println("Loaded topic from Database: " + topic));

        //see if any new topics in dropbox if none are stored locally:
        //(or required if this is first time app has run)
        new FlashCardTopicsDropBoxLoader(this::onFlashCardNameDropBoxLoaded).execute();
    }

    private void onFlashCardNameDropBoxLoaded(List<String> topics) {
        List<String> newTopics =
                topics.stream()
                        .filter(dropBoxTopic -> !loadedTopics.contains(dropBoxTopic))
                        .collect(Collectors.toList());

        loadedTopics.addAll(newTopics);
        refreshButtons();
        newTopics.forEach(topic -> System.out.println("Loaded topic from DropBox: " + topic));
    }

    int pageOffset = 0;
    int pageSize = 10;
    String nextPageText = "SEE NEXT PAGE >>";

    private void refreshButtons() {
        clearButtons();
        System.out.println("pageOffset " + pageOffset);
        System.out.println("loadedtopics " + loadedTopics.size());
        if(loadedTopics.size() > 10) {
            lastButton = addButton(nextPageText, lastButton);
            for(int x = pageOffset; x<=loadedTopics.size()-1 && x<(pageOffset + pageSize); x++) {
                lastButton = addButton(loadedTopics.get(x), lastButton);
            }
        } else {
            addButtons(loadedTopics);
        }
    }

    private void showNextPage() {
        pageOffset += pageSize;
        if(pageOffset > loadedTopics.size()-1) {
            pageOffset = 0;
        }

        refreshButtons();
    }

    private void addButtons(List<String> names) {

        for(String name: names) {
            lastButton = addButton(name, lastButton);
        }
    }

    private Button addButton(String title, Button lastButton) {

        System.out.println("Adding Button " + title);
        ConstraintLayout layout = findViewById(R.id.qaFlashcardButtonLayout);
        ConstraintSet constraintSet = new ConstraintSet();

        Button newButton = new Button(this);
        newButton.setText(title);
        newButton.setHeight(50);
        newButton.setId(new Random().nextInt(99999999));
        newButton.setBackgroundColor(getResources().getColor(R.color.buttonBackground));
        newButton.setTextColor(getResources().getColor(R.color.text));

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

    private void clearButtons() {
        ConstraintLayout layout = findViewById(R.id.qaFlashcardButtonLayout);
        layout.removeAllViewsInLayout();
        lastButton = null;
    }



    public void onFlashcardSelection(String selection) {

        if(selection.equals(nextPageText)) {
            showNextPage();
            return;
        }

        RevisionDatabase revisionDatabase = ServiceFactory.getRevisionDatabase();

        new FlashCardDatabaseLoader(this::FlashCardDatabaseLoaderCallback).execute(selection);

        System.out.println(selection);
    }

    private void FlashCardDatabaseLoaderCallback(List<QAFlashCard> qaFlashCardList, String topic) {
        if(qaFlashCardList.size() == 0 ) {
            System.out.println("No flashcards stored locally, loading from DropBox...");
            new FlashCardDropBoxLoader(this::FlashCardDropBoxLoaderCallback).execute(topic);
        } else {
            System.out.println(qaFlashCardList.size() + " Flashcards loaded from local database");
            loadQAFlashCardsSettings(topic, qaFlashCardList);
        }
    }

    private void FlashCardDropBoxLoaderCallback(List<QAFlashCard> qaFlashCardList, String topic) {
        if(qaFlashCardList.size() == 0 ) {
            showMessage("Unable to load flashcards from device or DropBox");
        } else {
            System.out.println(qaFlashCardList.size() + " Flashcards loaded from dropbox");
            loadQAFlashCardsSettings(topic, qaFlashCardList);
        }
    }

    private void loadQAFlashCardsSettings(String topic, List<QAFlashCard> qaFlashCardList) {

        System.out.println("loadQAFlashCardsSettings");
        new FlashCardSettingsDatabaseLoader(qaFlashCardSettings -> onQAFlashCardsLoaded(topic, qaFlashCardList, qaFlashCardSettings)).execute(topic);
    }

    private void onQAFlashCardsLoaded(String topic, List<QAFlashCard> qaFlashCardList, List<QAFlashCardSetting> qaFlashCardSettingList) {
        qaFlashCardList.forEach(System.out::println);
        Intent intent = new Intent(this, QAFlashCardRevisionActivity.class);
        intent.putExtra("flashCards", new QAFlashCardsWrapper(topic, qaFlashCardList, qaFlashCardSettingList));
        System.out.println("Number of settings found: " + qaFlashCardSettingList.size());
        qaFlashCardSettingList.forEach(System.out::println);
        startActivity(intent);
    }

}
