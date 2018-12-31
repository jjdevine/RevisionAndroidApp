package com.jonathandevinesoftware.revisionapp.singleflashcardrevision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.database.SingleFlashCard;
import com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks.SingleFlashCardFileNameLoader;
import com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks.SingleFlashCardLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SingleFlashCardRevisionActivity extends BaseActivity {

    private String topic;
    private List<String> fileNames = new ArrayList<>();
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_flash_card_revision);
        topic = getIntent().getStringExtra("topic");

        if (topic == null) {
            showMessage("Topic is null!");
            return;
        }

        new SingleFlashCardFileNameLoader(this::onFileNameLoad).execute(topic);

        findViewById(R.id.sfcNextBtn).setOnClickListener(this::onNextClick);
        findViewById(R.id.sfcPrevBtn).setOnClickListener(this::onPrevClick);
    }

    private void onFileNameLoad(List<String> fileNames) {
        this.fileNames.addAll(fileNames);
        Collections.shuffle(this.fileNames);
        displayIndex();
    }

    private void displayIndex() {
        if(index >= fileNames.size()) {
            showMessage("No more flashcards to display!");
            return;
        }

        new SingleFlashCardLoader(this::onFlashCardLoad).execute(topic, fileNames.get(index));
    }

    private void onFlashCardLoad(Optional<SingleFlashCard> flashCard) {

        if(flashCard.isPresent()) {
            setTextViewText(R.id.sfcTitleText, flashCard.get().getTitle());
            setTextViewText(R.id.sfcMainText, flashCard.get().getMainText());
            setTextViewText(R.id.sfcCounterText, (index+1)+"");
        } else {
            showMessage("Flashcard was empty...");
        }
    }

    private void onNextClick(View view) {
        if(index >= fileNames.size()) {
            showMessage("No more flashcards to display!");
            return;
        }

        index++;
        displayIndex();
    }

    private void onPrevClick(View view) {
        if(index > 0 ) {
            index++;
            displayIndex();
        }
    }
}
