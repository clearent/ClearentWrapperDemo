package com.example.clearentwrapperdemo;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.clearent.idtech.android.wrapper.ClearentCredentials;
import com.clearent.idtech.android.wrapper.ClearentWrapper;

public class App extends Application {

    private final ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        initSdkWrapper();
    }

    private void initSdkWrapper() {
        clearentWrapper.setSdkCredentials(
                new ClearentWrapper.SdkCredentials(
                        Constants.PUBLIC_KEY_SANDBOX,
                        new ClearentCredentials.ApiCredentials(Constants.API_KEY_SANDBOX)
                )
        );

        clearentWrapper.initializeSDK(
                getApplicationContext(),
                Constants.BASE_URL_SANDBOX,
                null,
                true // enable enhanced messages
        );
    }
}
