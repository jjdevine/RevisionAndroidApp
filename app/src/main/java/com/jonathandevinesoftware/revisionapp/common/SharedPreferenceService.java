package com.jonathandevinesoftware.revisionapp.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceService {

    public static SharedPreferences getSharedPreferences() {
        return App.getContext().getSharedPreferences("com.jonathandevinesoftware.revisionapp", Context.MODE_PRIVATE);
    }
}
