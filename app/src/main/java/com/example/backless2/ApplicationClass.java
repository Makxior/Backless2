package com.example.backless2;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "CBC0146D-9AA9-E2BA-FF94-84FA1E72F100";
    public static final String API_KEY = "05717941-FFE9-C22E-FF15-15A0B6B8D900";
    public static final String SERVER_URL = "https://api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl(SERVER_URL);
        Backendless.initApp(getApplicationContext(), APPLICATION_ID, API_KEY);
    }
}
