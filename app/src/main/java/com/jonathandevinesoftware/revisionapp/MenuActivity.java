package com.jonathandevinesoftware.revisionapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.android.Auth;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.dropbox.DropboxService;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.QAFlashcardSelectActivity;

import java.util.Optional;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button singleFlashCardButton = findViewById(R.id.buttonSingleFlashcard);
        Button qaFlashCardButton = findViewById(R.id.buttonQAFlashcard);
        Button dbxLoginButton = findViewById(R.id.buttonDbxLogin);

        singleFlashCardButton.setOnClickListener(this::onSingleFlashCardClick);
        qaFlashCardButton.setOnClickListener(this::onQAFlashCardClick);
        dbxLoginButton.setOnClickListener(this::onDropboxLoginClick);
    }

    public void onSingleFlashCardClick(View view) {
        showMessage("Single FlashCard Select");
    }

    public void onQAFlashCardClick(View view) {
        if(!isDropBoxConnected()) {
            showMessage("Connect your dropbox!");
            return;
        }

        Intent intent = new Intent(this, QAFlashcardSelectActivity.class);
        startActivity(intent);
    }

    public boolean isDropBoxConnected() {
        return getAccessToken().isPresent();
    }

    public void onDropboxLoginClick(View view) {
        initiateDbxLogin();
        Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.APP_KEY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDbxLoginInitiated() && !storeAccessToken()) {
            showMessage("Couldn't get access token");
        }
    }

    private boolean storeAccessToken() {
        String accessToken = Auth.getOAuth2Token();

        if(accessToken != null) {
            getPrivatePreferences().edit().putString(DropboxService.DBX_OAUTH_TOKEN, accessToken).apply();
            return true;
        }
        return false;
    }

    private Optional<String> getAccessToken() {
        SharedPreferences preferences = getPrivatePreferences();

        if(preferences == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(preferences.getString(DropboxService.DBX_OAUTH_TOKEN, null));
    }

    private void initiateDbxLogin() {
        getPrivatePreferences().edit().putBoolean(DropboxService.DBX_OAUTH_INITIATED, true).apply();
    }

    private boolean isDbxLoginInitiated() {
        return Boolean.TRUE.equals(getPrivatePreferences().getBoolean(DropboxService.DBX_OAUTH_INITIATED, false));
    }



}
