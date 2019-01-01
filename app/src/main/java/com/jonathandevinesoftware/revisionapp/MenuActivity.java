package com.jonathandevinesoftware.revisionapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.android.Auth;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.common.ServiceFactory;
import com.jonathandevinesoftware.revisionapp.database.tasks.CleardownDataTask;
import com.jonathandevinesoftware.revisionapp.dropbox.DropboxService;
import com.jonathandevinesoftware.revisionapp.qaflashcardselect.QAFlashcardSelectActivity;
import com.jonathandevinesoftware.revisionapp.singleflashcardrevision.SingleFlashCardTopicSelectActivity;

import java.util.Optional;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button singleFlashCardButton = findViewById(R.id.buttonSingleFlashcard);
        Button qaFlashCardButton = findViewById(R.id.buttonQAFlashcard);
        Button dbxLoginButton = findViewById(R.id.buttonDbxLogin);
        Button clearCacheButton = findViewById(R.id.buttonClearLocalCache);

        singleFlashCardButton.setOnClickListener(this::onSingleFlashCardClick);
        qaFlashCardButton.setOnClickListener(this::onQAFlashCardClick);
        dbxLoginButton.setOnClickListener(this::onDropBoxLoginClick);
        clearCacheButton.setOnClickListener(this::onClearLocalCacheClick);
    }

    private void onClearLocalCacheClick(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("Are you sure you want to clear all locally cached data?")
                .setTitle("Clear Local Data")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new CleardownDataTask(MenuActivity.this::clearDownDataCallback).execute();
                    }
                })
                .setNegativeButton("No", null).show();


    }

    private void clearDownDataCallback(Boolean success) {

        String alertTitle;
        String alertMessage;

        if(Boolean.TRUE == success) {
            alertTitle = "Local Data Cleared";
            alertMessage = "Local cache cleared successfully!";
        } else {
            alertTitle = "Local Data Failed to Clear";
            alertMessage = "Unable to clear local cache!";
        }

        new AlertDialog.Builder(this).setTitle(alertTitle).setMessage(alertMessage).show();
    }

    private void onSingleFlashCardClick(View view) {
        Intent intent = new Intent(this, SingleFlashCardTopicSelectActivity.class);
        startActivity(intent);
    }

    private void onQAFlashCardClick(View view) {
        if(!isDropBoxConnected()) {
            showMessage("Connect your dropbox!");
            return;
        }

        Intent intent = new Intent(this, QAFlashcardSelectActivity.class);
        startActivity(intent);
    }

    private boolean isDropBoxConnected() {
        return getAccessToken().isPresent();
    }

    private void onDropBoxLoginClick(View view) {
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
