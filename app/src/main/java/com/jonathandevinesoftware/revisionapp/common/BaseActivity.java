package com.jonathandevinesoftware.revisionapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

    protected void showMessage(String text) {
        App.showMessage(text);
    }

    protected SharedPreferences getPrivatePreferences() {
        return SharedPreferenceService.getSharedPreferences();
    }

    protected void setTextViewText(int id, String text) {
        ((TextView)findViewById(id)).setText(text);
    }

    protected void makeTextViewScrollable(int id) {
        ((TextView)findViewById(id)).setMovementMethod(new ScrollingMovementMethod());
    }
}
