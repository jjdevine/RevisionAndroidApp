package com.jonathandevinesoftware.revisionapp.qaflashcard;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jonathandevinesoftware.revisionapp.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QAFlashcard extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qaflashcard);

        addButtons(getButtonNames());
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

        //TODO: match size of button to parent view size

        Button newButton = new Button(this);
        newButton.setText(title);
        newButton.setHeight(50);
        newButton.setWidth(200);
        newButton.setId(new Random().nextInt(99999999));

    //    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50);
        layout.addView(newButton);

        constraintSet.clone(layout);

        View connectedTo = layout;
        int connectedEdge = ConstraintSet.TOP;

        if(lastButton != null) {
            connectedTo = lastButton;
            connectedEdge = ConstraintSet.BOTTOM;
        }

        constraintSet.connect(newButton.getId(), ConstraintSet.TOP, connectedTo.getId(), connectedEdge, 8);
        constraintSet.connect(newButton.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT, 8);
        constraintSet.connect(newButton.getId(), ConstraintSet.RIGHT, layout.getId(), ConstraintSet.RIGHT, 8);
        constraintSet.applyTo(layout);

        return newButton;
    }

    private List<String> getButtonNames() {

        return Arrays.asList("Button 1", "Button 2", "Button 3");

    }

}
