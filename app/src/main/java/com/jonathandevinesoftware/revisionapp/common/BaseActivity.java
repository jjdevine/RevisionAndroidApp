package com.jonathandevinesoftware.revisionapp.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class BaseActivity extends Activity {

    protected void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    protected SharedPreferences getPrivatePreferences() {
        return SharedPreferenceService.getSharedPreferences();
    }
}
