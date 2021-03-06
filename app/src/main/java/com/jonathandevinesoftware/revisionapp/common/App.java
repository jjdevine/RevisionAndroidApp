package com.jonathandevinesoftware.revisionapp.common;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class App extends Application {

    public static String LONG_MESSAGE = "ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\nACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG ACBDEFG ABCDEFG .\n\n";

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    public static void showMessage(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static boolean hasNetworkAccess() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return  isConnected;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
