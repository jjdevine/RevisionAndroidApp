package com.jonathandevinesoftware.revisionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.jonathandevinesoftware.revisionapp.common.BaseActivity;
import com.jonathandevinesoftware.revisionapp.dropbox.DropboxService;
import com.jonathandevinesoftware.revisionapp.qaflashcard.QAFlashcardActivity;

import java.util.Locale;
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
        Intent intent = new Intent(this, QAFlashcardActivity.class);
        startActivity(intent);
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

        if(getAccessToken().isPresent()) {
            System.out.println("TOKEN IS PRESENT");
            String token = getAccessToken().get();
            new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        readDropboxDirectory(token);
                    }
                }).start();
        }
    }

    private void readDropboxDirectory(String token) {
        System.out.println("Read dropbox directory");
        DbxRequestConfig config = new DbxRequestConfig("revisionApp", Locale.getDefault().toString());
        DbxClientV2 client  = new DbxClientV2(config, token);


        try {
            client.files().listFolder("").getEntries().forEach(System.out::println);
        } catch (DbxException e) {
            e.printStackTrace();
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
        return Optional.of(getPrivatePreferences().getString(DropboxService.DBX_OAUTH_TOKEN, null));
    }

    private void initiateDbxLogin() {
        getPrivatePreferences().edit().putBoolean(DropboxService.DBX_OAUTH_INITIATED, true).apply();
    }

    private boolean isDbxLoginInitiated() {
        return Boolean.TRUE.equals(getPrivatePreferences().getBoolean(DropboxService.DBX_OAUTH_INITIATED, false));
    }



}
