package com.jonathandevinesoftware.revisionapp.qaflashcardrevision;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QAFlashCardRevisionActivity extends BaseActivity {

    private enum State {ONE_DISPLAYED, BOTH_DISPLAYED}

    private QAFlashCardsWrapper qaFlashCardsWrapper;
    private int index = 0;
    private State state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaflash_card_revision);

        qaFlashCardsWrapper = (QAFlashCardsWrapper) getIntent().getSerializableExtra("flashCards");
        if(qaFlashCardsWrapper == null) {
            showMessage("FlashCards not found!");
        } else {
            init();
        }
    }

    private void init() {
        Collections.shuffle(qaFlashCardsWrapper.getQaFlashCardList());
        TextView titleTV = (TextView) findViewById(R.id.qaFlashCardRevisionTitleText);
        titleTV.setText(qaFlashCardsWrapper.getTopic());


        initIndex();

        findViewById(R.id.qaFlashCardNextBtn).setOnClickListener(this::onNextClick);
        findViewById(R.id.qaFlashCardFavouriteSwitch).setOnClickListener(this::onFavouriteClick);
    }

    /**
     * Sets the screen to the initial state for this index
     */
    private void initIndex() {
        state = State.ONE_DISPLAYED;
        showQuestion();
        ((TextView)findViewById(R.id.qaFlashCardIndexTV)).setText((index+1)+"");
    }

    private void showQuestion() {
        TextView questionTV = findViewById(R.id.qaFlashCardQuestionTV);
        questionTV.setText(qaFlashCardsWrapper.getQaFlashCardList().get(index).getQuestion());
    }

    private void showAnswer() {
        TextView answerTV = findViewById(R.id.qaFlashCardAnswerTV);
        answerTV.setText(qaFlashCardsWrapper.getQaFlashCardList().get(index).getAnswer());
    }

    private void hideQuestion() {
        TextView questionTV = findViewById(R.id.qaFlashCardQuestionTV);
        questionTV.setText("");
    }

    private void hideAnswer() {
        TextView answerTV = findViewById(R.id.qaFlashCardAnswerTV);
        answerTV.setText("");
    }

    private void onNextClick(View view) {
        switch (state) {
            case ONE_DISPLAYED:
                showQuestion();
                showAnswer();
                state = State.BOTH_DISPLAYED;
                break;
            case BOTH_DISPLAYED:
                hideQuestion();
                hideAnswer();
                index++;
                initIndex();
                break;

        }
    }

    private void onFavouriteClick(View view) {
        //TODO: this
    }


}
