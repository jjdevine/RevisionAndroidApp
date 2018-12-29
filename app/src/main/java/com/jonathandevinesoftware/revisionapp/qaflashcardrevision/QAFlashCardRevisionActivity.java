package com.jonathandevinesoftware.revisionapp.qaflashcardrevision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCard;
import com.jonathandevinesoftware.revisionapp.database.QAFlashCardDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QAFlashCardRevisionActivity extends BaseActivity {

    private enum State {ONE_DISPLAYED, BOTH_DISPLAYED}

    private QAFlashCardsWrapper qaFlashCardsWrapper;
    private QAFlashCardDAO qaFlashCardDAO;
    private int index = 0;
    private State state;
    private QAFlashCardSettingsService.RevealType revealType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaflash_card_revision);

        qaFlashCardDAO = ServiceFactory.getRevisionDatabase().qaFlashCardDAO();
        qaFlashCardsWrapper = (QAFlashCardsWrapper) getIntent().getSerializableExtra("flashCards");
        if(qaFlashCardsWrapper == null) {
            showMessage("FlashCards not found!");
        } else {
            init();
        }
    }

    private void init() {
        revealType = QAFlashCardSettingsService.getRevealType();
        System.out.println("Before shuffle");
        printFlashCards();
        shuffleFlashCards();
        System.out.println("After shuffle");
        printFlashCards();

        TextView titleTV = (TextView) findViewById(R.id.qaFlashCardRevisionTitleText);
        titleTV.setText(qaFlashCardsWrapper.getTopic());


        initIndex();

        findViewById(R.id.qaFlashCardNextBtn).setOnClickListener(this::onNextClick);
        findViewById(R.id.qaFlashCardFavouriteSwitch).setOnClickListener(this::onFavouriteClick);
        findViewById(R.id.qaFlashCardSettingBtn).setOnClickListener(this::onSettingsClick);
    }

    private void onSettingsClick(View view) {
        startActivity(new Intent(this, QAFlashCardSettingsActivity.class));
    }

    private void printFlashCards() {
        qaFlashCardsWrapper.getQaFlashCardList().forEach(System.out::println);
    }

    private void shuffleFlashCards() {
        List<QAFlashCard> favouriteFlashCards = new ArrayList<>();
        List<QAFlashCard> nonFavouriteFlashCards = new ArrayList<>();

        qaFlashCardsWrapper.getQaFlashCardList().forEach(flashCard -> {
            if(flashCard.isFavourite()) {
                favouriteFlashCards.add(flashCard);
            } else {
                nonFavouriteFlashCards.add(flashCard);
            }
        });

        //shuffle voth lists, but we need to favourites to always be before the non favourites
        Collections.shuffle(favouriteFlashCards);
        Collections.shuffle(nonFavouriteFlashCards);

        List<QAFlashCard> combinedList = new ArrayList<>();
        combinedList.addAll(favouriteFlashCards);
        combinedList.addAll(nonFavouriteFlashCards);

        qaFlashCardsWrapper.getQaFlashCardList().clear();
        qaFlashCardsWrapper.getQaFlashCardList().addAll(combinedList);
    }


    /**
     * Sets the screen to the initial state for this index
     */
    private void initIndex() {
        state = State.ONE_DISPLAYED;
        switch (revealType) {
            case QUESTION_FIRST:
                showQuestion();
                break;
            case ANSWER_FIRST:
                showAnswer();
                break;
            case ALTERNATE:
                if(index%2 == 0) {
                    showQuestion();
                } else {
                    showAnswer();
                }
        }

        ((TextView)findViewById(R.id.qaFlashCardIndexTV)).setText((index+1)+"");
        ((Switch)findViewById(R.id.qaFlashCardFavouriteSwitch)).setChecked(getCurrentFlashCard().isFavourite());
    }

    private void showQuestion() {
        TextView questionTV = findViewById(R.id.qaFlashCardQuestionTV);
        questionTV.setText(getCurrentFlashCard().getQuestion());
    }

    private void showAnswer() {
        TextView answerTV = findViewById(R.id.qaFlashCardAnswerTV);
        answerTV.setText(getCurrentFlashCard().getAnswer());
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
                if(index+1 == qaFlashCardsWrapper.getQaFlashCardList().size()) {
                    showMessage("No more flashcards left!");
                    return;
                }

                hideQuestion();
                hideAnswer();
                index++;
                initIndex();
                break;

        }
    }

    private void onFavouriteClick(View view) {
        Switch favouriteSwitch = (Switch) view;
        QAFlashCard qaFlashCard = getCurrentFlashCard();
        qaFlashCard.setFavourite(favouriteSwitch.isChecked());

        new Thread(
                () -> {
                    qaFlashCardDAO.update(qaFlashCard);
                }).start();
    }

    private QAFlashCard getCurrentFlashCard() {
        return qaFlashCardsWrapper.getQaFlashCardList().get(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("com.jonathandevinesoftware.revisionapp.qaflashcardrevision.QAFlashCardRevisionActivity.onResume");
        revealType = QAFlashCardSettingsService.getRevealType();
        System.out.println("Reveal type is : " + revealType.toString());
    }
}
