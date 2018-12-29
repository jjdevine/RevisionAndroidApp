package com.jonathandevinesoftware.revisionapp.qaflashcardrevision;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;

public class QAFlashCardSettingsActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaflash_card_settings);
        findViewById(R.id.qaFlashCardSettingDoneBtn).setOnClickListener(this::onDoneClick);

        QAFlashCardSettingsService.RevealType revealType = QAFlashCardSettingsService.getRevealType();

        switch (revealType) {
            case QUESTION_FIRST:
                checkCheckBox(R.id.qaFlashCardSettingsQuestionFirstRadio);
                break;
            case ANSWER_FIRST:
                checkCheckBox(R.id.qaFlashCardSettingsAnswerFirstRadio);
                break;
            case ALTERNATE:
                checkCheckBox(R.id.qaFlashCardSettingsAlternateRadio);
                break;
        }
    }

    private void checkCheckBox(int id) {
        ((RadioButton)findViewById(id)).setChecked(true);
    }

    private boolean isCheckBoxChecked(int id) {
        return ((RadioButton)findViewById(id)).isChecked();
    }

    private void onDoneClick(View view) {

        if(isCheckBoxChecked(R.id.qaFlashCardSettingsQuestionFirstRadio)) {
            QAFlashCardSettingsService.setRevealType(QAFlashCardSettingsService.RevealType.QUESTION_FIRST);
            System.out.println("setting reveal type QUESTION_FIRST");
        } else if(isCheckBoxChecked(R.id.qaFlashCardSettingsAnswerFirstRadio)) {
            QAFlashCardSettingsService.setRevealType(QAFlashCardSettingsService.RevealType.ANSWER_FIRST);
            System.out.println("setting reveal type ANSWER_FIRST");
        } else if(isCheckBoxChecked(R.id.qaFlashCardSettingsAlternateRadio)) {
            QAFlashCardSettingsService.setRevealType(QAFlashCardSettingsService.RevealType.ALTERNATE);
            System.out.println("setting reveal type ALTERNATE");
        }

        finish();

    }
}
