package com.jonathandevinesoftware.revisionapp.singleflashcardrevision;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jonathandevinesoftware.revisionapp.R;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.singleflashcardrevision.tasks.SingleFlashCardTopicsDropBoxLoader;

import java.util.List;
import java.util.Random;

public class SingleFlashCardTopicSelectActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_flash_card_topic_select);
        new SingleFlashCardTopicsDropBoxLoader(this::onTopicNameDropBoxLoad).execute();
    }

    private void onTopicNameDropBoxLoad(List<String> topics) {
        addButtons(topics);
    }

    private void addButtons(List<String> names) {

        Button lastButton = null;

        for(String name: names) {
            lastButton = addButton(name, lastButton);
        }
    }

    private Button addButton(String title, Button lastButton) {

        LinearLayout layout = findViewById(R.id.sfcTopicSelectLayout);

        Button newButton = new Button(this);
        newButton.setText(title);
        newButton.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        200));
        newButton.setId(new Random().nextInt(99999999));

        layout.addView(newButton);

        newButton.setOnClickListener(view -> onTopicSelection(title));

        return newButton;
    }

    public void onTopicSelection(String topic) {
        showMessage("selected " + topic);
    }

}
