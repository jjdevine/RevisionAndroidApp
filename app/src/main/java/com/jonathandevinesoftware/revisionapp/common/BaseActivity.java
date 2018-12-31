package com.jonathandevinesoftware.revisionapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
}
