package com.jonathandevinesoftware.revisionapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.DbxClientV2;
import com.jonathandevinesoftware.revisionapp.fileio.FileAccessor;
import com.jonathandevinesoftware.revisionapp.qaflashcard.QAFlashcard;

import java.util.Locale;
import java.util.Optional;

public class MenuActivity extends Activity {

    public static final String DBX_OAUTH_INITIATED = "dbx-oauth-initiated";
    public static final String DBX_OAUTH_TOKEN = "dbx-oauth-token";

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

      //  findViewById(R.layout.singleFlashCardButton);
    }

    public void onSingleFlashCardClick(View view) {
        showMessage("Single FlashCard Select");
     //   Intent intent = new Intent(this, RevisionActivity.class);
     //   startActivity(intent);
    }

    public void onQAFlashCardClick(View view) {

        Intent intent = new Intent(this, QAFlashcard.class);
        startActivity(intent);
    }

    public void onDropboxLoginClick(View view) {
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
        getAccessToken().ifPresent(token -> {

        });
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
            getPrivatePreferences().edit().putString(DBX_OAUTH_TOKEN, accessToken).apply();
            return true;
        }
        return false;
    }

    private Optional<String> getAccessToken() {
        return Optional.of(getPrivatePreferences().getString(DBX_OAUTH_TOKEN, null));
    }

    private void initiateDbxLogin() {
        getPrivatePreferences().edit().putBoolean(DBX_OAUTH_INITIATED, true).apply();
    }

    private boolean isDbxLoginInitiated() {
        return Boolean.TRUE.equals(getPrivatePreferences().getBoolean(DBX_OAUTH_INITIATED, false));
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private SharedPreferences getPrivatePreferences() {
        return getSharedPreferences("com.jonathandevinesoftware.revisionapp", Context.MODE_PRIVATE);
    }

}
